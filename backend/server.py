from fastapi import FastAPI, APIRouter, HTTPException
from dotenv import load_dotenv
from starlette.middleware.cors import CORSMiddleware
from motor.motor_asyncio import AsyncIOMotorClient
import os
import logging
from pathlib import Path
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
import uuid
from datetime import datetime
from enum import Enum

ROOT_DIR = Path(__file__).parent
load_dotenv(ROOT_DIR / '.env')

# MongoDB connection
mongo_url = os.environ['MONGO_URL']
client = AsyncIOMotorClient(mongo_url)
db = client[os.environ['DB_NAME']]

# Create the main app without a prefix
app = FastAPI(title="Rigour Construction Services API", version="1.0.0")

# Create a router with the /api prefix
api_router = APIRouter(prefix="/api")

# Enums for better type safety
class ProjectStatus(str, Enum):
    PENDING = "pending"
    IN_PROGRESS = "in_progress"
    COMPLETED = "completed"
    CANCELLED = "cancelled"

class ServiceCategory(str, Enum):
    MECHANICAL_ELECTRICAL = "mechanical_electrical"
    WOOD_WORKS = "wood_works"
    MASONRY = "masonry"
    ENVIRONMENTAL = "environmental"
    LOGISTICS = "logistics"

class UserRole(str, Enum):
    CLIENT = "client"
    PROVIDER = "provider"
    ADMIN = "admin"

# Pydantic Models
class User(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    name: str
    email: str
    phone: str
    role: UserRole
    avatar_url: Optional[str] = None
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

class UserCreate(BaseModel):
    name: str
    email: str
    phone: str
    role: UserRole
    avatar_url: Optional[str] = None

class ServiceProvider(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    user_id: str
    profession: str
    categories: List[ServiceCategory]
    rating: float = 0.0
    reviews_count: int = 0
    completed_jobs: int = 0
    description: Optional[str] = None
    hourly_rate: Optional[float] = None
    availability: bool = True
    location: Optional[str] = None
    skills: List[str] = []
    portfolio_images: List[str] = []
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

class ServiceProviderCreate(BaseModel):
    user_id: str
    profession: str
    categories: List[ServiceCategory]
    description: Optional[str] = None
    hourly_rate: Optional[float] = None
    location: Optional[str] = None
    skills: List[str] = []
    portfolio_images: List[str] = []

class Project(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    title: str
    description: str
    client_id: str
    provider_id: Optional[str] = None
    category: ServiceCategory
    status: ProjectStatus = ProjectStatus.PENDING
    budget: Optional[float] = None
    estimated_completion: Optional[datetime] = None
    actual_completion: Optional[datetime] = None
    location: Optional[str] = None
    images: List[str] = []
    progress_percentage: int = 0
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

class ProjectCreate(BaseModel):
    title: str
    description: str
    client_id: str
    category: ServiceCategory
    budget: Optional[float] = None
    estimated_completion: Optional[datetime] = None
    location: Optional[str] = None
    images: List[str] = []

class ProjectUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    provider_id: Optional[str] = None
    status: Optional[ProjectStatus] = None
    budget: Optional[float] = None
    estimated_completion: Optional[datetime] = None
    actual_completion: Optional[datetime] = None
    location: Optional[str] = None
    images: Optional[List[str]] = None
    progress_percentage: Optional[int] = None

class Chat(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    project_id: str
    participants: List[str]  # user IDs
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

class Message(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    chat_id: str
    sender_id: str
    content: str
    message_type: str = "text"  # text, image, file
    timestamp: datetime = Field(default_factory=datetime.utcnow)

class MessageCreate(BaseModel):
    chat_id: str
    sender_id: str
    content: str
    message_type: str = "text"

class Review(BaseModel):
    id: str = Field(default_factory=lambda: str(uuid.uuid4()))
    project_id: str
    client_id: str
    provider_id: str
    rating: int  # 1-5 stars
    comment: Optional[str] = None
    created_at: datetime = Field(default_factory=datetime.utcnow)

class ReviewCreate(BaseModel):
    project_id: str
    client_id: str
    provider_id: str
    rating: int
    comment: Optional[str] = None

# API Routes

# Health check
@api_router.get("/")
async def root():
    return {"message": "Rigour Construction Services API is running"}

# User Management
@api_router.post("/users", response_model=User)
async def create_user(user: UserCreate):
    user_dict = user.dict()
    user_obj = User(**user_dict)
    result = await db.users.insert_one(user_obj.dict())
    return user_obj

@api_router.get("/users/{user_id}", response_model=User)
async def get_user(user_id: str):
    user = await db.users.find_one({"id": user_id})
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return User(**user)

@api_router.get("/users", response_model=List[User])
async def get_users():
    users = await db.users.find().to_list(1000)
    return [User(**user) for user in users]

# Service Provider Management
@api_router.post("/providers", response_model=ServiceProvider)
async def create_provider(provider: ServiceProviderCreate):
    provider_dict = provider.dict()
    provider_obj = ServiceProvider(**provider_dict)
    result = await db.providers.insert_one(provider_obj.dict())
    return provider_obj

@api_router.get("/providers", response_model=List[ServiceProvider])
async def get_providers(category: Optional[ServiceCategory] = None):
    query = {}
    if category:
        query["categories"] = {"$in": [category]}
    
    providers = await db.providers.find(query).to_list(1000)
    return [ServiceProvider(**provider) for provider in providers]

@api_router.get("/providers/{provider_id}", response_model=ServiceProvider)
async def get_provider(provider_id: str):
    provider = await db.providers.find_one({"id": provider_id})
    if not provider:
        raise HTTPException(status_code=404, detail="Provider not found")
    return ServiceProvider(**provider)

# Project Management
@api_router.post("/projects", response_model=Project)
async def create_project(project: ProjectCreate):
    project_dict = project.dict()
    project_obj = Project(**project_dict)
    result = await db.projects.insert_one(project_obj.dict())
    return project_obj

@api_router.get("/projects", response_model=List[Project])
async def get_projects(
    client_id: Optional[str] = None,
    provider_id: Optional[str] = None,
    status: Optional[ProjectStatus] = None
):
    query = {}
    if client_id:
        query["client_id"] = client_id
    if provider_id:
        query["provider_id"] = provider_id
    if status:
        query["status"] = status
    
    projects = await db.projects.find(query).to_list(1000)
    return [Project(**project) for project in projects]

@api_router.get("/projects/{project_id}", response_model=Project)
async def get_project(project_id: str):
    project = await db.projects.find_one({"id": project_id})
    if not project:
        raise HTTPException(status_code=404, detail="Project not found")
    return Project(**project)

@api_router.put("/projects/{project_id}", response_model=Project)
async def update_project(project_id: str, project_update: ProjectUpdate):
    update_dict = {k: v for k, v in project_update.dict().items() if v is not None}
    update_dict["updated_at"] = datetime.utcnow()
    
    result = await db.projects.update_one(
        {"id": project_id},
        {"$set": update_dict}
    )
    
    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="Project not found")
    
    updated_project = await db.projects.find_one({"id": project_id})
    return Project(**updated_project)

# Chat Management
@api_router.post("/chats", response_model=Chat)
async def create_chat(project_id: str, participants: List[str]):
    chat_obj = Chat(project_id=project_id, participants=participants)
    result = await db.chats.insert_one(chat_obj.dict())
    return chat_obj

@api_router.get("/chats/{chat_id}", response_model=Chat)
async def get_chat(chat_id: str):
    chat = await db.chats.find_one({"id": chat_id})
    if not chat:
        raise HTTPException(status_code=404, detail="Chat not found")
    return Chat(**chat)

@api_router.get("/chats", response_model=List[Chat])
async def get_user_chats(user_id: str):
    chats = await db.chats.find({"participants": {"$in": [user_id]}}).to_list(1000)
    return [Chat(**chat) for chat in chats]

# Message Management
@api_router.post("/messages", response_model=Message)
async def create_message(message: MessageCreate):
    message_dict = message.dict()
    message_obj = Message(**message_dict)
    result = await db.messages.insert_one(message_obj.dict())
    return message_obj

@api_router.get("/messages/{chat_id}", response_model=List[Message])
async def get_messages(chat_id: str):
    messages = await db.messages.find({"chat_id": chat_id}).sort("timestamp", 1).to_list(1000)
    return [Message(**message) for message in messages]

# Review Management
@api_router.post("/reviews", response_model=Review)
async def create_review(review: ReviewCreate):
    review_dict = review.dict()
    review_obj = Review(**review_dict)
    result = await db.reviews.insert_one(review_obj.dict())
    
    # Update provider's rating
    await update_provider_rating(review.provider_id)
    
    return review_obj

@api_router.get("/reviews/{provider_id}", response_model=List[Review])
async def get_provider_reviews(provider_id: str):
    reviews = await db.reviews.find({"provider_id": provider_id}).to_list(1000)
    return [Review(**review) for review in reviews]

# Helper Functions
async def update_provider_rating(provider_id: str):
    """Update provider's average rating based on reviews"""
    reviews = await db.reviews.find({"provider_id": provider_id}).to_list(1000)
    if reviews:
        total_rating = sum(review["rating"] for review in reviews)
        avg_rating = total_rating / len(reviews)
        
        await db.providers.update_one(
            {"id": provider_id},
            {"$set": {"rating": round(avg_rating, 1), "reviews_count": len(reviews)}}
        )

# Search functionality
@api_router.get("/search/providers")
async def search_providers(
    query: str,
    category: Optional[ServiceCategory] = None,
    location: Optional[str] = None
):
    """Search providers by name, profession, or skills"""
    search_query = {
        "$or": [
            {"profession": {"$regex": query, "$options": "i"}},
            {"skills": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}}
        ]
    }
    
    if category:
        search_query["categories"] = {"$in": [category]}
    
    if location:
        search_query["location"] = {"$regex": location, "$options": "i"}
    
    providers = await db.providers.find(search_query).to_list(100)
    return [ServiceProvider(**provider) for provider in providers]

@api_router.get("/search/projects")
async def search_projects(query: str, client_id: Optional[str] = None):
    """Search projects by title or description"""
    search_query = {
        "$or": [
            {"title": {"$regex": query, "$options": "i"}},
            {"description": {"$regex": query, "$options": "i"}}
        ]
    }
    
    if client_id:
        search_query["client_id"] = client_id
    
    projects = await db.projects.find(search_query).to_list(100)
    return [Project(**project) for project in projects]

# Include the router in the main app
app.include_router(api_router)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_credentials=True,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

@app.on_event("shutdown")
async def shutdown_db_client():
    client.close()

# Add some sample data initialization
@app.on_event("startup")
async def startup_event():
    logger.info("Rigour Construction Services API started")
    
    # Create indexes for better performance
    await db.users.create_index("email", unique=True)
    await db.providers.create_index("categories")
    await db.projects.create_index("client_id")
    await db.projects.create_index("provider_id")
    await db.messages.create_index("chat_id")
    await db.reviews.create_index("provider_id")
    
    logger.info("Database indexes created")