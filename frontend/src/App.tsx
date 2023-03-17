import Login from "./components/login/Login";
import SignUp from "./components/signup/SignUp";
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
function App() {
  return (
      <BrowserRouter>
      <Routes>
        
        <Route index element={<Navigate to="/login" />}/>
        <Route path="/login" Component={Login}/>
        <Route path="/register" Component={SignUp} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;