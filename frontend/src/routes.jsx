import App from "./pages/App";
import ErrorPage from "./pages/ErrorPage";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import PostsPage from "./pages/PostsPage";
import MyAccountPage from "./pages/MyAccountPage";
import Logout from "./pages/Logout";

const routes = [
    {
        path: '/',
        element: <App/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                index: true,
                element: <HomePage/>
            },
            {
                path: 'login',
                element: <LoginPage/>
            },
            {
                path: 'register',
                element: <RegisterPage/>
            },
            {
                path: 'posts',
                element: <PostsPage/>
            },
            {
                path: 'my-account',
                element: <MyAccountPage/>
            },
            {
                path: 'logout',
                element: <Logout/>
            }
        ]
    }
]

export default routes;