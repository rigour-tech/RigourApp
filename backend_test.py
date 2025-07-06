#!/usr/bin/env python3
import requests
import json
import time
from datetime import datetime, timedelta
import uuid
import sys

# Get the backend URL from the frontend .env file
BACKEND_URL = "https://8a9e21de-ec89-41da-8847-16935db8455a.preview.emergentagent.com/api"

# Test data
test_users = [
    {
        "name": "John Adebayo",
        "email": "john.adebayo@example.com",
        "phone": "+2348012345678",
        "role": "client",
        "avatar_url": "https://randomuser.me/api/portraits/men/1.jpg"
    },
    {
        "name": "Amina Okafor",
        "email": "amina.okafor@example.com",
        "phone": "+2348023456789",
        "role": "provider",
        "avatar_url": "https://randomuser.me/api/portraits/women/2.jpg"
    },
    {
        "name": "Chidi Nwachukwu",
        "email": "chidi.nwachukwu@example.com",
        "phone": "+2348034567890",
        "role": "provider",
        "avatar_url": "https://randomuser.me/api/portraits/men/3.jpg"
    }
]

test_providers = [
    {
        "profession": "Electrician",
        "categories": ["mechanical_electrical"],
        "description": "Experienced electrician with 10+ years in residential and commercial projects",
        "hourly_rate": 5000.0,
        "location": "Lagos, Nigeria",
        "skills": ["Wiring", "Circuit installation", "Troubleshooting"],
        "portfolio_images": ["https://example.com/portfolio1.jpg", "https://example.com/portfolio2.jpg"]
    },
    {
        "profession": "Carpenter",
        "categories": ["wood_works"],
        "description": "Skilled carpenter specializing in custom furniture and cabinetry",
        "hourly_rate": 4500.0,
        "location": "Abuja, Nigeria",
        "skills": ["Furniture making", "Cabinetry", "Wood finishing"],
        "portfolio_images": ["https://example.com/portfolio3.jpg", "https://example.com/portfolio4.jpg"]
    }
]

test_projects = [
    {
        "title": "Home Electrical Rewiring",
        "description": "Complete rewiring of a 3-bedroom apartment in Lekki",
        "category": "mechanical_electrical",
        "budget": 250000.0,
        "estimated_completion": (datetime.utcnow() + timedelta(days=14)).isoformat(),
        "location": "Lekki, Lagos",
        "images": ["https://example.com/project1.jpg"]
    },
    {
        "title": "Custom Kitchen Cabinets",
        "description": "Design and installation of custom kitchen cabinets for a new home",
        "category": "wood_works",
        "budget": 350000.0,
        "estimated_completion": (datetime.utcnow() + timedelta(days=21)).isoformat(),
        "location": "Maitama, Abuja",
        "images": ["https://example.com/project2.jpg"]
    }
]

# Store created resources for later tests
created_users = []
created_providers = []
created_projects = []
created_chats = []
created_messages = []
created_reviews = []

def print_separator(title):
    """Print a separator with a title for better readability"""
    print("\n" + "=" * 80)
    print(f" {title} ".center(80, "="))
    print("=" * 80 + "\n")

def test_health_check():
    """Test the health check endpoint"""
    print_separator("Testing Health Check")
    
    try:
        response = requests.get(f"{BACKEND_URL}/")
        print(f"Status Code: {response.status_code}")
        print(f"Response: {response.json()}")
        
        assert response.status_code == 200
        assert "message" in response.json()
        assert "Rigour Construction Services API is running" in response.json()["message"]
        
        print("✅ Health check test passed")
        return True
    except Exception as e:
        print(f"❌ Health check test failed: {str(e)}")
        return False

def test_user_management():
    """Test user creation, retrieval, and listing"""
    print_separator("Testing User Management")
    
    try:
        # Create users
        for user_data in test_users:
            response = requests.post(f"{BACKEND_URL}/users", json=user_data)
            print(f"Create User Status Code: {response.status_code}")
            
            assert response.status_code == 200
            user = response.json()
            print(f"Created user: {user['name']} (ID: {user['id']})")
            created_users.append(user)
        
        # Get a specific user
        user_id = created_users[0]["id"]
        response = requests.get(f"{BACKEND_URL}/users/{user_id}")
        print(f"Get User Status Code: {response.status_code}")
        
        assert response.status_code == 200
        user = response.json()
        assert user["id"] == user_id
        print(f"Retrieved user: {user['name']}")
        
        # List all users
        response = requests.get(f"{BACKEND_URL}/users")
        print(f"List Users Status Code: {response.status_code}")
        
        assert response.status_code == 200
        users = response.json()
        assert len(users) >= len(created_users)
        print(f"Listed {len(users)} users")
        
        print("✅ User management tests passed")
        return True
    except Exception as e:
        print(f"❌ User management tests failed: {str(e)}")
        return False

def test_provider_management():
    """Test provider creation, listing, and category filtering"""
    print_separator("Testing Service Provider Management")
    
    try:
        # Create providers (using the provider users we created)
        for i, provider_data in enumerate(test_providers):
            # Use the provider users we created (index 1 and 2)
            provider_data["user_id"] = created_users[i+1]["id"]
            
            response = requests.post(f"{BACKEND_URL}/providers", json=provider_data)
            print(f"Create Provider Status Code: {response.status_code}")
            
            assert response.status_code == 200
            provider = response.json()
            print(f"Created provider: {provider['profession']} (ID: {provider['id']})")
            created_providers.append(provider)
        
        # Get a specific provider
        provider_id = created_providers[0]["id"]
        response = requests.get(f"{BACKEND_URL}/providers/{provider_id}")
        print(f"Get Provider Status Code: {response.status_code}")
        
        assert response.status_code == 200
        provider = response.json()
        assert provider["id"] == provider_id
        print(f"Retrieved provider: {provider['profession']}")
        
        # List all providers
        response = requests.get(f"{BACKEND_URL}/providers")
        print(f"List Providers Status Code: {response.status_code}")
        
        assert response.status_code == 200
        providers = response.json()
        assert len(providers) >= len(created_providers)
        print(f"Listed {len(providers)} providers")
        
        # Filter providers by category
        category = "mechanical_electrical"
        response = requests.get(f"{BACKEND_URL}/providers?category={category}")
        print(f"Filter Providers Status Code: {response.status_code}")
        
        assert response.status_code == 200
        filtered_providers = response.json()
        for provider in filtered_providers:
            assert category in provider["categories"]
        print(f"Filtered {len(filtered_providers)} providers by category '{category}'")
        
        print("✅ Provider management tests passed")
        return True
    except Exception as e:
        print(f"❌ Provider management tests failed: {str(e)}")
        return False

def test_project_management():
    """Test project creation, updates, and status tracking"""
    print_separator("Testing Project Management")
    
    try:
        # Create projects (using the client user we created)
        for project_data in test_projects:
            # Use the client user we created
            project_data["client_id"] = created_users[0]["id"]
            
            response = requests.post(f"{BACKEND_URL}/projects", json=project_data)
            print(f"Create Project Status Code: {response.status_code}")
            
            assert response.status_code == 200
            project = response.json()
            print(f"Created project: {project['title']} (ID: {project['id']})")
            created_projects.append(project)
        
        # Get a specific project
        project_id = created_projects[0]["id"]
        response = requests.get(f"{BACKEND_URL}/projects/{project_id}")
        print(f"Get Project Status Code: {response.status_code}")
        
        assert response.status_code == 200
        project = response.json()
        assert project["id"] == project_id
        print(f"Retrieved project: {project['title']}")
        
        # Update a project
        update_data = {
            "provider_id": created_providers[0]["id"],
            "status": "in_progress",
            "progress_percentage": 25
        }
        
        response = requests.put(f"{BACKEND_URL}/projects/{project_id}", json=update_data)
        print(f"Update Project Status Code: {response.status_code}")
        
        assert response.status_code == 200
        updated_project = response.json()
        assert updated_project["provider_id"] == update_data["provider_id"]
        assert updated_project["status"] == update_data["status"]
        assert updated_project["progress_percentage"] == update_data["progress_percentage"]
        print(f"Updated project: {updated_project['title']} (Status: {updated_project['status']}, Progress: {updated_project['progress_percentage']}%)")
        
        # List projects with filters
        client_id = created_users[0]["id"]
        response = requests.get(f"{BACKEND_URL}/projects?client_id={client_id}")
        print(f"List Client Projects Status Code: {response.status_code}")
        
        assert response.status_code == 200
        client_projects = response.json()
        for project in client_projects:
            assert project["client_id"] == client_id
        print(f"Listed {len(client_projects)} projects for client")
        
        provider_id = created_providers[0]["id"]
        response = requests.get(f"{BACKEND_URL}/projects?provider_id={provider_id}")
        print(f"List Provider Projects Status Code: {response.status_code}")
        
        assert response.status_code == 200
        provider_projects = response.json()
        for project in provider_projects:
            assert project["provider_id"] == provider_id
        print(f"Listed {len(provider_projects)} projects for provider")
        
        print("✅ Project management tests passed")
        return True
    except Exception as e:
        print(f"❌ Project management tests failed: {str(e)}")
        return False

def test_chat_messaging():
    """Test chat creation and message handling"""
    print_separator("Testing Chat and Messaging System")
    
    try:
        # Create a chat for the first project
        project_id = created_projects[0]["id"]
        participants = [created_users[0]["id"], created_providers[0]["id"]]  # client and provider
        
        response = requests.post(f"{BACKEND_URL}/chats?project_id={project_id}", json=participants)
        print(f"Create Chat Status Code: {response.status_code}")
        
        assert response.status_code == 200
        chat = response.json()
        print(f"Created chat for project: {project_id} (Chat ID: {chat['id']})")
        created_chats.append(chat)
        
        # Send messages in the chat
        chat_id = chat["id"]
        messages = [
            {
                "chat_id": chat_id,
                "sender_id": created_users[0]["id"],
                "content": "Hello, I'd like to discuss the project details.",
                "message_type": "text"
            },
            {
                "chat_id": chat_id,
                "sender_id": created_providers[0]["id"],
                "content": "Sure, I'm available to discuss. What would you like to know?",
                "message_type": "text"
            }
        ]
        
        for message_data in messages:
            response = requests.post(f"{BACKEND_URL}/messages", json=message_data)
            print(f"Create Message Status Code: {response.status_code}")
            
            assert response.status_code == 200
            message = response.json()
            print(f"Sent message: {message['content'][:30]}... (ID: {message['id']})")
            created_messages.append(message)
        
        # Get messages for the chat
        response = requests.get(f"{BACKEND_URL}/messages/{chat_id}")
        print(f"Get Messages Status Code: {response.status_code}")
        
        assert response.status_code == 200
        chat_messages = response.json()
        assert len(chat_messages) >= len(messages)
        print(f"Retrieved {len(chat_messages)} messages for chat")
        
        # Get user chats
        user_id = created_users[0]["id"]
        response = requests.get(f"{BACKEND_URL}/chats?user_id={user_id}")
        print(f"Get User Chats Status Code: {response.status_code}")
        
        assert response.status_code == 200
        user_chats = response.json()
        for chat in user_chats:
            assert user_id in chat["participants"]
        print(f"Retrieved {len(user_chats)} chats for user")
        
        print("✅ Chat and messaging tests passed")
        return True
    except Exception as e:
        print(f"❌ Chat and messaging tests failed: {str(e)}")
        return False

def test_review_rating():
    """Test review creation and provider rating updates"""
    print_separator("Testing Review and Rating System")
    
    try:
        # Create a review for the first project
        review_data = {
            "project_id": created_projects[0]["id"],
            "client_id": created_users[0]["id"],
            "provider_id": created_providers[0]["id"],
            "rating": 5,
            "comment": "Excellent work! Very professional and completed the job on time."
        }
        
        response = requests.post(f"{BACKEND_URL}/reviews", json=review_data)
        print(f"Create Review Status Code: {response.status_code}")
        
        assert response.status_code == 200
        review = response.json()
        print(f"Created review: Rating {review['rating']}/5 (ID: {review['id']})")
        created_reviews.append(review)
        
        # Check if provider rating was updated
        provider_id = created_providers[0]["id"]
        response = requests.get(f"{BACKEND_URL}/providers/{provider_id}")
        print(f"Get Provider Status Code: {response.status_code}")
        
        assert response.status_code == 200
        provider = response.json()
        assert provider["rating"] > 0
        assert provider["reviews_count"] > 0
        print(f"Provider rating updated: {provider['rating']}/5 ({provider['reviews_count']} reviews)")
        
        # Get provider reviews
        response = requests.get(f"{BACKEND_URL}/reviews/{provider_id}")
        print(f"Get Provider Reviews Status Code: {response.status_code}")
        
        assert response.status_code == 200
        provider_reviews = response.json()
        assert len(provider_reviews) > 0
        print(f"Retrieved {len(provider_reviews)} reviews for provider")
        
        print("✅ Review and rating tests passed")
        return True
    except Exception as e:
        print(f"❌ Review and rating tests failed: {str(e)}")
        return False

def test_search_functionality():
    """Test provider and project search capabilities"""
    print_separator("Testing Search Functionality")
    
    try:
        # Search for providers
        query = "Electrician"
        response = requests.get(f"{BACKEND_URL}/search/providers?query={query}")
        print(f"Search Providers Status Code: {response.status_code}")
        
        assert response.status_code == 200
        search_results = response.json()
        print(f"Found {len(search_results)} providers matching '{query}'")
        
        # Search with category filter
        category = "mechanical_electrical"
        response = requests.get(f"{BACKEND_URL}/search/providers?query={query}&category={category}")
        print(f"Search Providers with Category Status Code: {response.status_code}")
        
        assert response.status_code == 200
        filtered_results = response.json()
        for provider in filtered_results:
            assert category in provider["categories"]
        print(f"Found {len(filtered_results)} providers matching '{query}' in category '{category}'")
        
        # Search for projects
        query = "Kitchen"
        response = requests.get(f"{BACKEND_URL}/search/projects?query={query}")
        print(f"Search Projects Status Code: {response.status_code}")
        
        assert response.status_code == 200
        search_results = response.json()
        print(f"Found {len(search_results)} projects matching '{query}'")
        
        # Search with client filter
        client_id = created_users[0]["id"]
        response = requests.get(f"{BACKEND_URL}/search/projects?query={query}&client_id={client_id}")
        print(f"Search Projects with Client Status Code: {response.status_code}")
        
        assert response.status_code == 200
        filtered_results = response.json()
        for project in filtered_results:
            assert project["client_id"] == client_id
        print(f"Found {len(filtered_results)} projects matching '{query}' for client")
        
        print("✅ Search functionality tests passed")
        return True
    except Exception as e:
        print(f"❌ Search functionality tests failed: {str(e)}")
        return False

def run_all_tests():
    """Run all tests and return results"""
    results = {}
    
    print("\n🔍 Starting Rigour Construction Services API Tests\n")
    
    # Core API Infrastructure
    results["Core API Infrastructure"] = test_health_check()
    
    # User Management System
    results["User Management System"] = test_user_management()
    
    # Service Provider Management
    results["Service Provider Management"] = test_provider_management()
    
    # Project Management System
    results["Project Management System"] = test_project_management()
    
    # Chat and Messaging System
    results["Chat and Messaging System"] = test_chat_messaging()
    
    # Review and Rating System
    results["Review and Rating System"] = test_review_rating()
    
    # Search Functionality
    results["Search Functionality"] = test_search_functionality()
    
    # Print summary
    print_separator("Test Results Summary")
    
    all_passed = True
    for test_name, passed in results.items():
        status = "✅ PASSED" if passed else "❌ FAILED"
        print(f"{test_name}: {status}")
        if not passed:
            all_passed = False
    
    if all_passed:
        print("\n🎉 All tests passed successfully!")
    else:
        print("\n❌ Some tests failed. Check the logs for details.")
    
    return results

if __name__ == "__main__":
    run_all_tests()