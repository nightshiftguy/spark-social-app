# Running project locally #
To run project on your machine clone the repo and follow below instructions for backend and frontend
### Set up Backend ###
1. Copy env.sample and name it .env then link it as enviroment variables file in your IDE
2. Download and run Docker to enable easy database setup
3. Download and use JDK 26+ (Liberica preffered)
4. Create Cloudinary account for image storage
    - create cloud project and copy api keys and cloud name to dotenv
    ```
    CLOUDINARY_CLOUD_NAME=<cloud-name>
    CLOUDINARY_API_KEY=<api-key>
    CLOUDINARY_API_SECRET=<api-secret>
    ```
    - create folder for images:
    ```
    CLOUD_UPLOAD_FOLDER=<images-folder>
    ```

5. To run the application locally create run configuration for Spring Boot app or use commands below in backend folder:
```
./mvnw spring-boot:run       # macOS/Linux
mvnw.cmd spring-boot:run     # Windows
```
Use `generated-requests.http` for testing endpoints

### Set up Frontend ###
1. Copy env.sample and name it .env
2. Install node and npm to check installation run:
```
node -v
npm -v
```
3. Install js modules (npm packages):
```
npm install -D
```
4. Run Frontend locally:
```
npm run dev
```
Frontend should be hosted on `localhost:5173` by default