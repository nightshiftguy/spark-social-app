import getPayloadFromJWT from "./getPayloadFromJWT";
export default function extractUsernameFromJWT(jwt){
    return getPayloadFromJWT(jwt).sub;
}