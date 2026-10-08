import logo from '../assets/spark_logo.svg';
import './Logo.css';

export default function Logo(){
    return (
        <div className="logo">
            <img className="logo-image" src={logo} alt="Spark logo" />
            <h1>Spark</h1>
        </div>
    );
}