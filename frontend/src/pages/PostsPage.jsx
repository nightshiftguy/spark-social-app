import { useApiFetch } from '../api';
import { useMemo } from 'react';

function PostsPage() {
   const routeAndOptions = useMemo(()=>{return { route: "posts", options: {} }}, []);
   const {data, error, loading} = useApiFetch(routeAndOptions.route, routeAndOptions.options);
   console.log(data)
  return (
    <>
      <h1>PostsPage</h1>
      {loading && <p>Loading...</p>}
      {data?.content?.map(post => (
        <div key={post.id}>{post.textContent}</div>
      ))}
      {error && <p>A network error was encountered: {error.message}</p>}
    </>
  )
}

export default PostsPage