import React, { useState, useEffect } from "react";
import "./App.css";
import axios from "axios";

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
const API = `${BACKEND_URL}/api`;

// Sample data for demonstration
const serviceCategories = [
  {
    id: 1,
    name: "Mechanical and Electrical Services",
    icon: "⚡",
    subcategories: ["Plumbing", "Electrical Installation", "HVAC", "Generator Installation"]
  },
  {
    id: 2,
    name: "Wood Works and Finishing",
    icon: "🪵",
    subcategories: ["Painting", "Plastering", "Tiling", "Insulation", "Carpentry", "Flooring"]
  },
  {
    id: 3,
    name: "Masonry",
    icon: "🧱",
    subcategories: ["Brickwork", "Concrete Work", "Stone Work", "Block Work"]
  },
  {
    id: 4,
    name: "Environ. and Specialized Services",
    icon: "🌿",
    subcategories: ["Landscaping", "Cleaning Services", "Pest Control", "Security Systems"]
  },
  {
    id: 5,
    name: "Logistics and Machineries",
    icon: "🚚",
    subcategories: ["Equipment Rental", "Material Supply", "Transportation", "Storage"]
  }
];

const projectTypes = [
  "Renovation",
  "New Build",
  "Repair",
  "Maintenance",
  "Installation",
  "Landscaping",
  "Interior Design",
  "Exterior Work"
];

const ongoingProjects = [
  {
    id: 1,
    title: "Kitchen cabinet installation",
    image: "https://images.pexels.com/photos/3990359/pexels-photo-3990359.jpeg",
    provider: {
      name: "Lukpak Johnson",
      profession: "Plumber",
      avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=50&h=50&fit=crop&crop=face"
    },
    completion: "in 3 days",
    progress: 75,
    type: "Renovation",
    category: "wood_works"
  }
];

const discoverItems = [
  {
    id: 1,
    title: "Stair-work Façade in Ikeja",
    image: "https://images.unsplash.com/photo-1659353589462-d407d429a4df?crop=entropy&cs=srgb&fm=jpg&ixid=M3w3NTY2Njd8MHwxfHNlYXJjaHwxfHxjb25zdHJ1Y3Rpb24lMjBzZXJ2aWNlc3xlbnwwfHx8dGVhbHwxNzUxODAwNDM4fDA&ixlib=rb-4.1.0&q=85",
    rating: 4.5,
    description: "Stair-work Façade in Ikeja. Schedule your appointment to get professional service.",
    installers: ["John D.", "Mary S.", "Alex K."],
    installersCount: 3
  },
  {
    id: 2,
    title: "Auto Tile Cutting Plug-in Jigsaw",
    image: "https://images.pexels.com/photos/1249611/pexels-photo-1249611.jpeg",
    rating: 4.9,
    description: "START installation and get an installation service from the best installation.",
    installers: ["Mike R.", "Sarah P."],
    installersCount: 2
  },
  {
    id: 3,
    title: "Solar Tracking PV Systems Jiag",
    image: "https://images.pexels.com/photos/30592246/pexels-photo-30592246.jpeg",
    rating: 4.8,
    description: "Solar installations delivered professionally by our skilled installers.",
    installers: ["David L.", "Emma W.", "Tom B."],
    installersCount: 3
  }
];

const sampleProviders = [
  {
    id: 1,
    name: "Adebayo Olanrewaju",
    profession: "Master Electrician",
    avatar: "https://images.pexels.com/photos/8486973/pexels-photo-8486973.jpeg",
    rating: 4.9,
    completedProjects: 127,
    verified: true,
    eta: "15 mins",
    location: { lat: 6.5244, lng: 3.3792 },
    category: "mechanical_electrical",
    hourlyRate: 5500,
    skills: ["Wiring", "Circuit Installation", "Solar Systems"]
  },
  {
    id: 2,
    name: "Fatima Aliyu",
    profession: "Interior Designer",
    avatar: "https://images.unsplash.com/photo-1494790108755-2616b332c98c?w=50&h=50&fit=crop&crop=face",
    rating: 4.8,
    completedProjects: 89,
    verified: true,
    eta: "22 mins",
    location: { lat: 6.5344, lng: 3.3892 },
    category: "wood_works",
    hourlyRate: 4200,
    skills: ["Interior Design", "Space Planning", "Furniture Selection"]
  },
  {
    id: 3,
    name: "Chinedu Okoro",
    profession: "Mason & Contractor",
    avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=50&h=50&fit=crop&crop=face",
    rating: 4.7,
    completedProjects: 156,
    verified: true,
    eta: "18 mins",
    location: { lat: 6.5144, lng: 3.3692 },
    category: "masonry",
    hourlyRate: 3800,
    skills: ["Brickwork", "Concrete", "Foundation"]
  }
];

const App = () => {
  const [activeTab, setActiveTab] = useState('home');
  const [currentScreen, setCurrentScreen] = useState('home');
  const [expandedCategory, setExpandedCategory] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState('Regal');
  
  // Project creation states
  const [projectData, setProjectData] = useState({
    title: '',
    type: '',
    description: '',
    images: [],
    documents: [],
    budget: '',
    location: ''
  });
  
  // Service provider matching states
  const [selectedProject, setSelectedProject] = useState('');
  const [smartMatch, setSmartMatch] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [scheduleTime, setScheduleTime] = useState('');
  const [customFilter, setCustomFilter] = useState('');
  const [filteredProviders, setFilteredProviders] = useState(sampleProviders);
  const [selectedProvider, setSelectedProvider] = useState(null);

  useEffect(() => {
    // Test backend connection
    const testBackend = async () => {
      try {
        const response = await axios.get(`${API}/`);
        console.log('Backend connected:', response.data.message);
      } catch (error) {
        console.error('Backend connection failed:', error);
      }
    };
    testBackend();
  }, []);

  const toggleCategory = (categoryId) => {
    setExpandedCategory(expandedCategory === categoryId ? null : categoryId);
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  const handleStartProject = () => {
    setCurrentScreen('project-creation');
  };

  const handleFileUpload = (event, type) => {
    const files = Array.from(event.target.files);
    if (type === 'images') {
      // Convert to base64 for preview
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          setProjectData(prev => ({
            ...prev,
            images: [...prev.images, { file, preview: e.target.result, name: file.name }]
          }));
        };
        reader.readAsDataURL(file);
      });
    } else if (type === 'documents') {
      setProjectData(prev => ({
        ...prev,
        documents: [...prev.documents, ...files]
      }));
    }
  };

  const removeFile = (index, type) => {
    setProjectData(prev => ({
      ...prev,
      [type]: prev[type].filter((_, i) => i !== index)
    }));
  };

  const handleChooseProvider = () => {
    setCurrentScreen('provider-matching');
  };

  const handleFilterProviders = () => {
    let filtered = sampleProviders;
    
    if (selectedCategory) {
      filtered = filtered.filter(provider => provider.category === selectedCategory);
    }
    
    if (searchQuery) {
      filtered = filtered.filter(provider => 
        provider.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        provider.profession.toLowerCase().includes(searchQuery.toLowerCase()) ||
        provider.skills.some(skill => skill.toLowerCase().includes(searchQuery.toLowerCase()))
      );
    }
    
    setFilteredProviders(filtered);
    if (filtered.length > 0) {
      setSelectedProvider(filtered[0]);
    }
  };

  const handleSendRequest = async () => {
    if (!selectedProvider || !selectedProject) return;
    
    try {
      // Create project if it doesn't exist
      const projectResponse = await axios.post(`${API}/projects`, {
        title: projectData.title || `Project with ${selectedProvider.name}`,
        description: projectData.description || `${selectedProvider.profession} service`,
        client_id: "client_123", // This would come from auth
        category: selectedProvider.category,
        budget: parseFloat(projectData.budget) || null,
        location: projectData.location || "Lagos, Nigeria",
        images: projectData.images.map(img => img.preview)
      });
      
      alert(`Request sent to ${selectedProvider.name}! Project created successfully.`);
      setCurrentScreen('home');
    } catch (error) {
      console.error('Error sending request:', error);
      alert('Error sending request. Please try again.');
    }
  };

  // Project Creation Screen
  const ProjectCreationScreen = () => (
    <div className="project-creation-screen">
      <div className="header">
        <button className="back-btn" onClick={() => setCurrentScreen('home')}>
          ← Back
        </button>
        <h1>Create New Project</h1>
      </div>
      
      <div className="project-form">
        <div className="form-group">
          <label>Project Title</label>
          <input
            type="text"
            placeholder="Enter project title"
            value={projectData.title}
            onChange={(e) => setProjectData(prev => ({ ...prev, title: e.target.value }))}
            className="form-input"
          />
        </div>
        
        <div className="form-group">
          <label>Project Type</label>
          <select
            value={projectData.type}
            onChange={(e) => setProjectData(prev => ({ ...prev, type: e.target.value }))}
            className="form-select"
          >
            <option value="">Select project type</option>
            {projectTypes.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>
        
        <div className="form-group">
          <label>Project Description</label>
          <textarea
            placeholder="Describe the current condition of your site and the work you want done..."
            value={projectData.description}
            onChange={(e) => setProjectData(prev => ({ ...prev, description: e.target.value }))}
            className="form-textarea"
            rows="4"
          />
        </div>
        
        <div className="form-group">
          <label>Budget (₦)</label>
          <input
            type="number"
            placeholder="Enter estimated budget"
            value={projectData.budget}
            onChange={(e) => setProjectData(prev => ({ ...prev, budget: e.target.value }))}
            className="form-input"
          />
        </div>
        
        <div className="form-group">
          <label>Location</label>
          <input
            type="text"
            placeholder="Enter project location"
            value={projectData.location}
            onChange={(e) => setProjectData(prev => ({ ...prev, location: e.target.value }))}
            className="form-input"
          />
        </div>
        
        <div className="form-group">
          <label>Upload Images</label>
          <div className="upload-section">
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={(e) => handleFileUpload(e, 'images')}
              className="file-input"
              id="images-upload"
            />
            <label htmlFor="images-upload" className="upload-btn">
              📷 Choose Images
            </label>
            <div className="image-previews">
              {projectData.images.map((img, index) => (
                <div key={index} className="image-preview">
                  <img src={img.preview} alt="Preview" />
                  <button
                    className="remove-btn"
                    onClick={() => removeFile(index, 'images')}
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
        
        <div className="form-group">
          <label>Upload Documents</label>
          <div className="upload-section">
            <input
              type="file"
              multiple
              accept=".pdf,.doc,.docx,.txt"
              onChange={(e) => handleFileUpload(e, 'documents')}
              className="file-input"
              id="documents-upload"
            />
            <label htmlFor="documents-upload" className="upload-btn">
              📄 Choose Documents
            </label>
            <div className="document-list">
              {projectData.documents.map((doc, index) => (
                <div key={index} className="document-item">
                  <span className="document-name">{doc.name}</span>
                  <button
                    className="remove-btn"
                    onClick={() => removeFile(index, 'documents')}
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
        
        <button className="choose-provider-btn" onClick={handleChooseProvider}>
          Choose a Service Provider
        </button>
      </div>
    </div>
  );

  // Service Provider Matching Screen
  const ProviderMatchingScreen = () => (
    <div className="provider-matching-screen">
      <div className="header">
        <button className="back-btn" onClick={() => setCurrentScreen('project-creation')}>
          ← Back
        </button>
        <h1>Find Service Provider</h1>
      </div>
      
      <div className="matching-form">
        <div className="form-group">
          <label>Select Project</label>
          <select
            value={selectedProject}
            onChange={(e) => setSelectedProject(e.target.value)}
            className="form-select"
          >
            <option value="">Choose a project</option>
            <option value="current">Current Project: {projectData.title || 'New Project'}</option>
            {ongoingProjects.map(project => (
              <option key={project.id} value={project.id}>{project.title}</option>
            ))}
          </select>
        </div>
        
        <div className="smart-match">
          <label className="checkbox-label">
            <input
              type="checkbox"
              checked={smartMatch}
              onChange={(e) => setSmartMatch(e.target.checked)}
            />
            <span className="checkmark"></span>
            Recommend list of services for my project
          </label>
        </div>
        
        <div className="filters-section">
          <h3>Filters</h3>
          
          <div className="form-group">
            <label>Category</label>
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              className="form-select"
            >
              <option value="">All Categories</option>
              <option value="mechanical_electrical">Mechanical & Electrical</option>
              <option value="wood_works">Wood Works & Finishing</option>
              <option value="masonry">Masonry</option>
              <option value="environmental">Environmental & Specialized</option>
              <option value="logistics">Logistics & Machineries</option>
            </select>
          </div>
          
          <div className="form-group">
            <label>Schedule Time</label>
            <input
              type="datetime-local"
              value={scheduleTime}
              onChange={(e) => setScheduleTime(e.target.value)}
              className="form-input"
            />
          </div>
          
          <div className="form-group">
            <label>Custom Filter</label>
            <select
              value={customFilter}
              onChange={(e) => setCustomFilter(e.target.value)}
              className="form-select"
            >
              <option value="">Additional filters</option>
              <option value="rating">Highest Rated</option>
              <option value="price">Lowest Price</option>
              <option value="distance">Nearest</option>
              <option value="experience">Most Experienced</option>
            </select>
          </div>
          
          <button className="filter-btn" onClick={handleFilterProviders}>
            Filter Service Providers
          </button>
        </div>
        
        {selectedProvider && (
          <div className="provider-preview">
            <h3>Recommended Provider</h3>
            <div className="provider-card">
              <div className="provider-header">
                <img src={selectedProvider.avatar} alt={selectedProvider.name} className="provider-avatar" />
                <div className="provider-info">
                  <h4>{selectedProvider.name}</h4>
                  {selectedProvider.verified && <span className="verified-badge">✓ Verified</span>}
                  <p className="provider-profession">{selectedProvider.profession}</p>
                </div>
              </div>
              
              <div className="provider-stats">
                <div className="stat">
                  <span className="stat-value">{selectedProvider.rating}</span>
                  <span className="stat-label">Rating</span>
                </div>
                <div className="stat">
                  <span className="stat-value">{selectedProvider.completedProjects}</span>
                  <span className="stat-label">Projects</span>
                </div>
                <div className="stat">
                  <span className="stat-value">{selectedProvider.eta}</span>
                  <span className="stat-label">ETA</span>
                </div>
              </div>
              
              <div className="provider-actions">
                <button className="chat-btn">💬 Chat</button>
                <button className="call-btn">📞 Call</button>
              </div>
              
              <div className="provider-skills">
                <h5>Skills:</h5>
                <div className="skills-list">
                  {selectedProvider.skills.map((skill, index) => (
                    <span key={index} className="skill-tag">{skill}</span>
                  ))}
                </div>
              </div>
              
              <div className="provider-rate">
                <span className="rate-label">Hourly Rate:</span>
                <span className="rate-value">₦{selectedProvider.hourlyRate.toLocaleString()}</span>
              </div>
            </div>
          </div>
        )}
        
        <div className="map-section">
          <h3>Location</h3>
          <div className="map-placeholder">
            <div className="map-content">
              <div className="location-pin user-pin">📍 Your Location</div>
              <div className="location-pin provider-pin">🔧 Provider Location</div>
              <p className="map-info">Interactive map showing provider locations</p>
            </div>
          </div>
        </div>
        
        <button className="send-request-btn" onClick={handleSendRequest}>
          Send Request
        </button>
      </div>
    </div>
  );

  const HomeScreen = () => (
    <div className="home-screen">
      {/* Header */}
      <div className="header">
        <div className="header-content">
          <div className="greeting">
            <h1>{userName}, Welcome!</h1>
          </div>
          <div className="header-actions">
            <button className="chat-icon">💬</button>
            {!isLoggedIn ? (
              <button className="login-btn" onClick={handleLogin}>Log In</button>
            ) : (
              <button className="logout-btn" onClick={handleLogout}>Log Out</button>
            )}
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="main-content">
        {/* Start New Project Button */}
        <button className="start-project-btn" onClick={handleStartProject}>
          Start new Project
        </button>

        {/* Search Services */}
        <div className="search-section">
          <input
            type="text"
            placeholder="Search Services"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="search-input"
          />
        </div>

        {/* Ongoing Projects */}
        <div className="ongoing-section">
          <h2>Ongoing</h2>
          {ongoingProjects.map(project => (
            <div key={project.id} className="project-card">
              <img src={project.image} alt={project.title} className="project-image" />
              <div className="project-info">
                <h3>{project.title}</h3>
                <div className="provider-info">
                  <img src={project.provider.avatar} alt={project.provider.name} className="provider-avatar" />
                  <div>
                    <p className="provider-name">{project.provider.name}</p>
                    <p className="provider-profession">{project.provider.profession}</p>
                  </div>
                  <div className="completion-time">
                    <span>Completion: {project.completion}</span>
                  </div>
                </div>
                <div className="project-actions">
                  <button className="view-history-btn">View Work History</button>
                  <button className="get-provider-btn">Get another Service Provider</button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Services Accordion */}
        <div className="services-section">
          <div className="services-header">
            <h2>Services</h2>
            <button className="view-all-btn">View All</button>
          </div>
          
          <div className="services-accordion">
            {serviceCategories.map(category => (
              <div key={category.id} className="service-category">
                <button 
                  className="category-header"
                  onClick={() => toggleCategory(category.id)}
                >
                  <div className="category-info">
                    <span className="category-icon">{category.icon}</span>
                    <span className="category-name">{category.name}</span>
                  </div>
                  <span className={`expand-icon ${expandedCategory === category.id ? 'expanded' : ''}`}>
                    {expandedCategory === category.id ? '−' : '+'}
                  </span>
                </button>
                {expandedCategory === category.id && (
                  <div className="category-content">
                    {category.subcategories.map((subcategory, index) => (
                      <button key={index} className="subcategory-item">
                        {subcategory}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Discover Section */}
        <div className="discover-section">
          <div className="discover-header">
            <h2>Discover</h2>
            <span className="discover-subtitle">Latest Products</span>
          </div>
          
          <div className="discover-grid">
            {discoverItems.map(item => (
              <div key={item.id} className="discover-card">
                <img src={item.image} alt={item.title} className="discover-image" />
                <div className="discover-content">
                  <h3>{item.title}</h3>
                  <div className="rating">
                    <span className="stars">★★★★★</span>
                    <span className="rating-value">{item.rating}</span>
                  </div>
                  <p className="description">{item.description}</p>
                  <div className="installers">
                    <div className="installer-avatars">
                      {item.installers.slice(0, 3).map((installer, index) => (
                        <div key={index} className="installer-avatar">
                          {installer.charAt(0)}
                        </div>
                      ))}
                    </div>
                    <span className="installer-count">{item.installersCount} installers close to you</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );

  const ProjectsScreen = () => (
    <div className="projects-screen">
      <div className="header">
        <h1>My Projects</h1>
      </div>
      <div className="projects-content">
        <div className="project-tabs">
          <button className="tab active">Active</button>
          <button className="tab">Completed</button>
          <button className="tab">Scheduled</button>
        </div>
        <div className="projects-list">
          {ongoingProjects.map(project => (
            <div key={project.id} className="project-item">
              <img src={project.image} alt={project.title} className="project-thumb" />
              <div className="project-details">
                <h3>{project.title}</h3>
                <p>Provider: {project.provider.name}</p>
                <p>Status: In Progress</p>
                <div className="progress-bar">
                  <div className="progress-fill" style={{width: `${project.progress}%`}}></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );

  const ChatsScreen = () => (
    <div className="chats-screen">
      <div className="header">
        <h1>My Chats</h1>
      </div>
      <div className="chats-content">
        <div className="chat-item">
          <img src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=50&h=50&fit=crop&crop=face" alt="Chat" className="chat-avatar" />
          <div className="chat-info">
            <h3>Lukpak Johnson</h3>
            <p>Kitchen cabinet installation discussion...</p>
            <span className="chat-time">2 hours ago</span>
          </div>
        </div>
      </div>
    </div>
  );

  const SupportScreen = () => (
    <div className="support-screen">
      <div className="header">
        <h1>Support</h1>
      </div>
      <div className="support-content">
        <div className="support-options">
          <button className="support-item">
            <span className="support-icon">❓</span>
            <span>FAQs</span>
          </button>
          <button className="support-item">
            <span className="support-icon">🎫</span>
            <span>Support Tickets</span>
          </button>
          <button className="support-item">
            <span className="support-icon">📞</span>
            <span>Contact Us</span>
          </button>
        </div>
      </div>
    </div>
  );

  const ProfileScreen = () => (
    <div className="profile-screen">
      <div className="header">
        <h1>Profile</h1>
      </div>
      <div className="profile-content">
        <div className="profile-info">
          <img src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=80&h=80&fit=crop&crop=face" alt="Profile" className="profile-avatar" />
          <h2>{userName}</h2>
          <p>Client Account</p>
        </div>
        <div className="profile-options">
          <button className="profile-item">
            <span className="profile-icon">👤</span>
            <span>Personal Information</span>
          </button>
          <button className="profile-item">
            <span className="profile-icon">💳</span>
            <span>Wallet</span>
          </button>
          <button className="profile-item">
            <span className="profile-icon">📋</span>
            <span>Past Jobs</span>
          </button>
          <button className="profile-item">
            <span className="profile-icon">⭐</span>
            <span>Saved Providers</span>
          </button>
          <button className="profile-item">
            <span className="profile-icon">🔒</span>
            <span>KYC Verification</span>
          </button>
        </div>
      </div>
    </div>
  );

  const renderScreen = () => {
    switch(currentScreen) {
      case 'project-creation':
        return <ProjectCreationScreen />;
      case 'provider-matching':
        return <ProviderMatchingScreen />;
      case 'home':
        return <HomeScreen />;
      case 'projects':
        return <ProjectsScreen />;
      case 'chats':
        return <ChatsScreen />;
      case 'support':
        return <SupportScreen />;
      case 'profile':
        return <ProfileScreen />;
      default:
        return <HomeScreen />;
    }
  };

  return (
    <div className="app">
      <div className="app-content">
        {renderScreen()}
      </div>
      
      {/* Bottom Navigation - only show on main screens */}
      {['home', 'projects', 'chats', 'support', 'profile'].includes(currentScreen) && (
        <div className="bottom-nav">
          <button 
            className={`nav-item ${currentScreen === 'home' ? 'active' : ''}`}
            onClick={() => setCurrentScreen('home')}
          >
            <span className="nav-icon">🏠</span>
            <span className="nav-label">Home</span>
          </button>
          <button 
            className={`nav-item ${currentScreen === 'projects' ? 'active' : ''}`}
            onClick={() => setCurrentScreen('projects')}
          >
            <span className="nav-icon">📋</span>
            <span className="nav-label">My Projects</span>
          </button>
          <button 
            className={`nav-item ${currentScreen === 'chats' ? 'active' : ''}`}
            onClick={() => setCurrentScreen('chats')}
          >
            <span className="nav-icon">💬</span>
            <span className="nav-label">My Chats</span>
          </button>
          <button 
            className={`nav-item ${currentScreen === 'support' ? 'active' : ''}`}
            onClick={() => setCurrentScreen('support')}
          >
            <span className="nav-icon">🛠️</span>
            <span className="nav-label">Support</span>
          </button>
          <button 
            className={`nav-item ${currentScreen === 'profile' ? 'active' : ''}`}
            onClick={() => setCurrentScreen('profile')}
          >
            <span className="nav-icon">👤</span>
            <span className="nav-label">Profile</span>
          </button>
        </div>
      )}
    </div>
  );
};

export default App;