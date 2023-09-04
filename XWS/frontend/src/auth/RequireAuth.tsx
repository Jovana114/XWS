// auth/RequireAuth.tsx
import { useLocation, Navigate, Outlet } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "./AuthContext"; // Correct the path to AuthContext if needed

interface RequireAuthProps {
  allowedRoles: string[];
}

const RequireAuth: React.FC<RequireAuthProps> = ({ allowedRoles }) => {
  const { auth } = useContext(AuthContext);
  const location = useLocation();

  const storedAuthData = localStorage.getItem("authData");
  if (!auth && (!storedAuthData || !hasRequiredAuthData(storedAuthData))) {
    return <Navigate to="/signin" state={{ from: location }} replace />;
  }

  if (auth && auth.accessToken && auth.roles && auth.id) {
    if (auth.roles.some((role) => allowedRoles.includes(role))) {
      return <Outlet />; // Render the nested route components
    }
  }

  return <Navigate to="/signin" state={{ from: location }} replace />;
};

const hasRequiredAuthData = (storedAuthData: any) => {
  try {
    const { accessToken, id, roles } = JSON.parse(storedAuthData);
    return accessToken && id && Array.isArray(roles) && roles.length > 0;
  } catch (error) {
    return false;
  }
};

export default RequireAuth;
