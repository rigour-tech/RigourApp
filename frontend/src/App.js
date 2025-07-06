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
    progress: 75
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

const App = () => {
  const [activeTab, setActiveTab] = useState('home');
  const [expandedCategory, setExpandedCategory] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState('Regal');

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
        <button className="start-project-btn">
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
    switch(activeTab) {
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
      
      {/* Bottom Navigation */}
      <div className="bottom-nav">
        <button 
          className={`nav-item ${activeTab === 'home' ? 'active' : ''}`}
          onClick={() => setActiveTab('home')}
        >
          <span className="nav-icon">🏠</span>
          <span className="nav-label">Home</span>
        </button>
        <button 
          className={`nav-item ${activeTab === 'projects' ? 'active' : ''}`}
          onClick={() => setActiveTab('projects')}
        >
          <span className="nav-icon">📋</span>
          <span className="nav-label">My Projects</span>
        </button>
        <button 
          className={`nav-item ${activeTab === 'chats' ? 'active' : ''}`}
          onClick={() => setActiveTab('chats')}
        >
          <span className="nav-icon">💬</span>
          <span className="nav-label">My Chats</span>
        </button>
        <button 
          className={`nav-item ${activeTab === 'support' ? 'active' : ''}`}
          onClick={() => setActiveTab('support')}
        >
          <span className="nav-icon">🛠️</span>
          <span className="nav-label">Support</span>
        </button>
        <button 
          className={`nav-item ${activeTab === 'profile' ? 'active' : ''}`}
          onClick={() => setActiveTab('profile')}
        >
          <span className="nav-icon">👤</span>
          <span className="nav-label">Profile</span>
        </button>
      </div>
    </div>
  );
};

export default App;