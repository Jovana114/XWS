import Login from "./components/login/Login";
import SignUp from "./components/signup/SignUp";
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import Home from "./components/home/HomeHost";
function App() {
  return (
      <BrowserRouter>
      <Routes>
        
        <Route index element={<Navigate to="/login" />}/>
        <Route path="/login" Component={Login}/>
        <Route path="/register" Component={SignUp} />
        <Route path="/home" Component={Home} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;