export default function getPayloadFromJWT(jwt){
        // A JWT has 3 parts separated by '.'
        // The middle part is a base64 encoded JSON
        // decode the base64 
        return JSON.parse(atob(jwt.split(".")[1]))
    }