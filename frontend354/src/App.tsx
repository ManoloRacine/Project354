import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [processedImage, setProcessedImage] = useState<string | null>(null);
  const [uploadResponse, setUploadResponse] = useState<string>('');

  const [resize, setResize] = useState(false);
  const [resizeWidth, setResizeWidth] = useState('');
  const [resizeHeight, setResizeHeight] = useState('');

  const [crop, setCrop] = useState(false);
  const [cropWidth, setCropWidth] = useState('');
  const [cropHeight, setCropHeight] = useState('');
  const [cropX, setCropX] = useState('');
  const [cropY, setCropY] = useState('');

  const [rotate, setRotate] = useState(false);
  const [rotateAngle, setRotateAngle] = useState('');

  const [flip, setFlip] = useState(false);
  const [flipH, setFlipH] = useState(false);
  const [flipV, setFlipV] = useState(false);

  const [quality, setQuality] = useState(false);
  const [qualityValue, setQualityValue] = useState('');

  const [bc, setBc] = useState(false);
  const [brightness, setBrightness] = useState('');
  const [contrast, setContrast] = useState('');

  const [format, setFormat] = useState(false);
  const [formatValue, setFormatValue] = useState('');

  useEffect(() => {
    if (selectedImage) {
      const objectUrl = URL.createObjectURL(selectedImage);
      setImagePreview(objectUrl);
      return () => URL.revokeObjectURL(objectUrl);
    }
  }, [selectedImage]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedImage(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedImage) {
      setUploadResponse('Please select an image first.');
      return;
    }
    const formData = new FormData();
    formData.append('file', selectedImage);
    try {
      const response = await fetch('http://localhost:8080/api/images/upload', {
        method: 'POST',
        body: formData,
      });
      const result = await response.json();
      if (!response.ok) {
        throw new Error(result.message || 'Upload failed');
      }
      setUploadResponse(result.message);
    } catch (error: any) {
      setUploadResponse(`Error: ${error.message}`);
    }
  };

  const handleApply = async () => {
    const operations = [];
    if (resize) operations.push({ type: 'resize', width: Number(resizeWidth), height: Number(resizeHeight) });
    if (crop) operations.push({ type: 'crop', width: Number(cropWidth), height: Number(cropHeight), x: Number(cropX), y: Number(cropY) });
    if (rotate) operations.push({ type: 'rotate', angle: Number(rotateAngle) });
    if (flip) operations.push({ type: 'flip', horizontal: flipH, vertical: flipV });
    if (quality) operations.push({ type: 'quality', quality: Number(qualityValue) });
    if (bc) operations.push({ type: 'brightness-contrast', brightness: Number(brightness), contrast: Number(contrast) });
    if (format) operations.push({ type: 'format', format: formatValue });
    console.log(JSON.stringify(operations));
    try {
      const response = await fetch('http://localhost:8080/api/images/apply', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': '*/*',
        },
        body: JSON.stringify(operations),
    });

        if (!response.ok) throw new Error(`Server responded with ${response.status}`);

        const result = await response.json();
        setUploadResponse(`${result.message}. Saved as: ${result.filename}`);
    } catch (error: any) {
        setUploadResponse(`Error: ${error.message}`);
    }
};

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">ImageMagick Tool</h1>
      <input type="file" onChange={handleFileChange} />
      {imagePreview && <img src={imagePreview} alt="preview" className="max-w-xs" />}
      <button onClick={handleUpload} className="bg-green-600 text-white px-4 py-2 rounded">Upload</button>
      <div className="space-y-2">
        <label><input type="checkbox" checked={resize} onChange={() => setResize(!resize)} /> Resize</label>
        {resize && (<div><input placeholder="Width" value={resizeWidth} onChange={e => setResizeWidth(e.target.value)} /> <input placeholder="Height" value={resizeHeight} onChange={e => setResizeHeight(e.target.value)} /></div>)}

        <label><input type="checkbox" checked={crop} onChange={() => setCrop(!crop)} /> Crop</label>
        {crop && (<div><input placeholder="Width" value={cropWidth} onChange={e => setCropWidth(e.target.value)} /> <input placeholder="Height" value={cropHeight} onChange={e => setCropHeight(e.target.value)} /> <input placeholder="X" value={cropX} onChange={e => setCropX(e.target.value)} /> <input placeholder="Y" value={cropY} onChange={e => setCropY(e.target.value)} /></div>)}

        <label><input type="checkbox" checked={rotate} onChange={() => setRotate(!rotate)} /> Rotate</label>
        {rotate && (<div><input placeholder="Angle" value={rotateAngle} onChange={e => setRotateAngle(e.target.value)} /></div>)}

        <label><input type="checkbox" checked={flip} onChange={() => setFlip(!flip)} /> Flip</label>
        {flip && (<div><label><input type="checkbox" checked={flipH} onChange={() => setFlipH(!flipH)} /> Horizontal</label> <label><input type="checkbox" checked={flipV} onChange={() => setFlipV(!flipV)} /> Vertical</label></div>)}

        <label><input type="checkbox" checked={quality} onChange={() => setQuality(!quality)} /> Quality</label>
        {quality && (<div><input placeholder="1-100" value={qualityValue} onChange={e => setQualityValue(e.target.value)} /></div>)}

        <label><input type="checkbox" checked={bc} onChange={() => setBc(!bc)} /> Brightness/Contrast</label>
        {bc && (<div><input placeholder="Brightness" value={brightness} onChange={e => setBrightness(e.target.value)} /> <input placeholder="Contrast" value={contrast} onChange={e => setContrast(e.target.value)} /></div>)}

        <label><input type="checkbox" checked={format} onChange={() => setFormat(!format)} /> Format</label>
        {format && (<div><input placeholder="png, jpg, etc." value={formatValue} onChange={e => setFormatValue(e.target.value)} /></div>)}
      </div>
      <button onClick={handleApply} className="bg-blue-600 text-white px-4 py-2 rounded">Apply Operations</button>
      {uploadResponse && <p>{uploadResponse}</p>}
      {processedImage && (<div><h2>Processed Image</h2><img src={processedImage} alt="processed" className="max-w-xs" /><a href={processedImage} download>Download Processed Image</a></div>)}
    </div>
  );
}

export default App;