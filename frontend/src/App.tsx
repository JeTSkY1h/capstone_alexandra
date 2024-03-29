import './App.css';
import { Route, Routes } from 'react-router-dom';
import Landing from './Pages/Landing';
import Register from './Pages/Register';
import Main from './Pages/Main';
import Login from './Pages/Login';
import BookPage from './Pages/BookPage';
import Reader from "./Pages/Reader/Reader"


function App() {
  return (
    <Routes>
        <Route path="/" element={<Landing/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/main/:query" element={<Main />} />
        <Route path="/main" element={<Main />} />
        <Route path="/reader/:id" element={<Reader />} />
        <Route path="/book/:id" element={<BookPage/>}/>
    </Routes>
  );
}

export default App;
