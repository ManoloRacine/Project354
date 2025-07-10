import { useState } from 'react';
import './App.css';
// 1. Import the new component
import ImageUploadComponent from './ImageUploadComponent';

function App() {
  const [count, setCount] = useState(0);
  // 2. Add state to hold the selected image file
  const [selectedImage, setSelectedImage] = useState<File | null>(null);

  // 3. Add the handler function to receive the file from the child component
  const handleImageUpload = (file: File) => {
    console.log('Image selected in App component:', file);
    setSelectedImage(file);
  };

  return (
    <>
      {/* --- image file upload component --- */}

      <h1>ImageMagick Tool</h1>

      <ImageUploadComponent onImageUpload={handleImageUpload} />

      {/* This section will appear after an image has been selected */}
      {selectedImage && (
        <div className="image-preview-container">
          <h2>Selected Image</h2>
          <p>File Name: {selectedImage.name}</p>
        </div>
      )}

      {/* --- end of image file upload component --- */}



      <div className="card">
        <button onClick={() => fetch('http://localhost:8080/test')}>
          Press this button to call the backend
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
    </>
  );
}

export default App;