
import React, { useState, useEffect } from 'react';

// Define the props for our new component. It needs the image file to work with.
interface ImageProcessingComponentProps {
    selectedImage: File;
}

const ImageProcessingComponent: React.FC<ImageProcessingComponentProps> = ({ selectedImage }) => {
    // All state related to resizing is now self-contained in this component.
    const [width, setWidth] = useState('150');
    const [height, setHeight] = useState('150');
    const [resizedImage, setResizedImage] = useState<string | null>(null);
    const [isResizing, setIsResizing] = useState(false);
    const [error, setError] = useState<string>('');

    // When the selected image prop changes, we should clear out the old resized result.
    useEffect(() => {
        setResizedImage(null);
        setError('');
    }, [selectedImage]);

    // This function calls the backend to perform the resize operation.
    const handleResize = async () => {
        setIsResizing(true);
        setResizedImage(null);
        setError('');

        const formData = new FormData();
        formData.append('file', selectedImage);
        formData.append('width', width);
        formData.append('height', height);

        try {
            const response = await fetch('http://localhost:8080/resize', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Image resize failed');
            }

            const imageBlob = await response.blob();
            const objectUrl = URL.createObjectURL(imageBlob);
            setResizedImage(objectUrl);

        } catch (err: any) {
            setError(`Error: ${err.message}`);
        } finally {
            setIsResizing(false);
        }
    };

    return (
        <div className="processing-container">
            <hr style={{ margin: '2rem 0' }} />
            <h2>2. Process Your Image</h2>
            <p>Apply transformations to your selected image.</p>

            <div className="resize-controls">
                <label>Width: <input type="number" value={width} onChange={(e) => setWidth(e.target.value)} /></label>
                <label>Height: <input type="number" value={height} onChange={(e) => setHeight(e.target.value)} /></label>
            </div>

            <button onClick={handleResize} disabled={isResizing}>
                {isResizing ? 'Resizing...' : 'Resize Image'}
            </button>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            {/* Display the result of the resize operation */}
            {resizedImage && (
                <div className="processed-image-container">
                    <h4>Resized Image Result</h4>
                    <img src={resizedImage} alt="Resized result" className="image-preview" />
                </div>
            )}
        </div>
    );
};

export default ImageProcessingComponent;