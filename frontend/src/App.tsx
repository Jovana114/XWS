import Login from "./components/login/Login";
import SignUp from "./components/signup/SignUp";
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import HomeHost from "./components/home/HomeHost";
import HomeGuest from "./components/home/HomeGuest";
function App() {
  return (
      <BrowserRouter>
      <Routes>
        
        <Route index element={<Navigate to="/login" />}/>
        <Route path="/login" Component={Login}/>
        <Route path="/register" Component={SignUp} />
        <Route path="/homehost" Component={HomeHost} />
        <Route path="/homeguest" Component={HomeGuest} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;