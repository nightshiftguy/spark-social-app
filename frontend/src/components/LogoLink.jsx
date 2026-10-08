import { Link } from "react-router";
import logo from '../assets/spark_logo.svg';
import './LogoLink.css';

export default function LogoLink(){
    return (
        <Link className="logo-link" to="/">
            <h2>Spark</h2>
            <img className="logo-link-image" src={logo} alt="Spark logo" />
        </Link>
    );
}