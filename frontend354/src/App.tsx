import { useState, useEffect } from 'react';

import './App.css';
// 1. Import the new component
import ImageUploadComponent from './ImageUploadComponent';

function App() {
  const [count, setCount] = useState(0);
  // 2. Add state to hold the selected image file
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [uploadResponse, setUploadResponse] = useState<string>('');


    // 3. Add the handler function to receive the file from the child component
  const handleImageUpload = (file: File) => {
    console.log('Image selected in App component:', file);
    setSelectedImage(file);
  };
    // image preview
    useEffect(() => {
        if (selectedImage) {
            const objectUrl = URL.createObjectURL(selectedImage);
            setImagePreview(objectUrl);

            return () => URL.revokeObjectURL(objectUrl);
        }
    }, [selectedImage]);

    // handle upload to send to backend
    const handleUpload = async () => {
        if (!selectedImage) {
            setUploadResponse('Please select an image first.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedImage);

        try {
            // Update the URL to the correct endpoint
            const response = await fetch('http://localhost:8080/api/images/upload', {
                method: 'POST',
                body: formData,
            });

            // The backend now returns JSON
            const result = await response.json();

            if (!response.ok) {
                // Use the error message from the backend if available
                throw new Error(result.message || 'Upload failed');
            }

            // Display the success message from the backend response
            setUploadResponse(result.message);
        } catch (error) {
            console.error('Error uploading the image:', error);
            // Display a more specific error message
            setUploadResponse(`Error: ${error.message}`);
        }
    };


    return (
    <>
      {/* --- image file upload component --- */}

      <h1>ImageMagick Tool</h1>

      <ImageUploadComponent onImageUpload={handleImageUpload} />

      {/* This section will appear after an image has been selected */}
        {selectedImage && imagePreview && (
            <div className="image-preview-container">
                <h2>Selected Image Preview</h2>
                <img src={imagePreview} alt="Selected preview" className="image-preview" />
                <p>File Name: {selectedImage.name}</p>
                
                {/* button to trigger the upload */}
                <button onClick={handleUpload}>Upload to Server</button>

                {/*  display the server's response */}
                {uploadResponse && <p>{uploadResponse}</p>}
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