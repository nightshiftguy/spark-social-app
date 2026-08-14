import { Link } from "react-router";

export default function NavBar( {location, isLogged, loggedUserUsername} ){
    //don't render navbar on register and login pages
    if(location.pathname == "/register" || location.pathname == "/login"){
        return;
    }

    return (
        <div>
            <h1><Link to='/posts'>Spark</Link></h1>
            {!isLogged && <Link to='/login'>log in</Link>}
            {isLogged && <Link to='/logout'>log out</Link>}
            {isLogged && <Link to='/my-account'>{loggedUserUsername}</Link>}
        </div>
    )
}