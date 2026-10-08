import { Link } from "react-router";
import LogoLink from "./LogoLink"
import "./NavBar.css"

export default function NavBar( {location, isLogged, loggedUserUsername} ){
    let loginOrRegisterPage = location.pathname == "/register" || location.pathname == "/login";

    return (
        <nav className="navbar">
            <div className="left">
                <LogoLink/>
            </div>
            <div className="right">
                {!loginOrRegisterPage && !isLogged && <Link to='/login'>log in</Link>}
                {!loginOrRegisterPage && !isLogged && <Link to='/register'>create account</Link>}
                {!loginOrRegisterPage && isLogged && <Link to='/logout'>log out</Link>}
                {!loginOrRegisterPage && isLogged && <p>{loggedUserUsername}</p>}
            </div>
            </nav>
    )
}