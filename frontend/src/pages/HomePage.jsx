import { Link } from "react-router"
import Logo from "../components/Logo";
import './HomePage.css'

function HomePage() {
  return (
    <div className="home-container">
      <Logo/>
      
      <p>Create posts, comment and like. To start <Link to='/posts'>see posts</Link> or <Link to='/register'>register</Link> to create your own content.</p>
    </div>
  )
}

export default HomePage;