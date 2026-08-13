export default function NavBar( {location} ){
    //don't render navbar on register and login pages
    if(location.pathname == "/register" || location.pathname == "/login"){
        return;
    }

    return (
        <div>
            <h1>Spark</h1>
        </div>
    )
}