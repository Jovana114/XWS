// useAuth.tsx
import { useContext } from "react";
import { AuthContext, AuthContextValue } from "../auth/AuthContext";

const useAuth = (): AuthContextValue => {
  const { auth, loading, setLoading, setAuth, logout } =
    useContext(AuthContext);

  // Check if loading is true, then auth data is not yet available
  if (loading) {
    setLoading(true);
    return { auth: {}, loading, setLoading, setAuth, logout };
  }

  // If not loading, return the auth data, the logout function, and the role
  return { auth, loading, setLoading, setAuth, logout };
};

export default useAuth;
