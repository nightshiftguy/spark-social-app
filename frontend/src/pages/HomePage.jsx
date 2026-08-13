import { Link } from "react-router"

function HomePage() {
  return (
    <>
      <h1>HomePage</h1>
      <Link to='/posts'>see posts</Link>
    </>
  )
}

export default HomePage;