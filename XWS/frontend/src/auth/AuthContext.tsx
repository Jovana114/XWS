/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-empty-function */
import React, {
  createContext,
  useState,
  useEffect,
  SetStateAction,
} from "react";

interface AuthData {
  accessToken?: string;
  roles?: string[];
  id?: string;
}

interface AuthContextValue {
  auth: AuthData & { loading: boolean }; // Add loading property to auth
  setAuth: React.Dispatch<SetStateAction<AuthData>>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue>({
  auth: { loading: true }, // Set loading to true initially
  setAuth: () => {},
  logout: () => {},
});

export const AuthProvider: React.FC = ({ children }: any) => {
  const storedAuthData = localStorage.getItem("authData");
  const initialAuthData: AuthData = storedAuthData
    ? JSON.parse(storedAuthData)
    : {};

  const [auth, setAuth] = useState<AuthData & { loading: boolean }>({
    ...initialAuthData,
    loading: true, // Set loading to true initially
  });

  useEffect(() => {
    // Perform additional validation or expiration checks if needed
    const expirationDate = localStorage.getItem("expirationDate");
    if (
      !storedAuthData ||
      (expirationDate && new Date() > new Date(expirationDate))
    ) {
      logout(); // Clear auth data if not valid
    } else {
      setAuth((prevAuth) => ({ ...prevAuth, loading: false })); // Set loading to false when id is found
    }
  }, []);

  const updateAuthData: AuthContextValue["setAuth"] = (data: any) => {
    if (Object.keys(data).length === 0) {
      localStorage.removeItem("authData");
    } else {
      setAuth(data);
      localStorage.setItem("authData", JSON.stringify(data));
    }
  };

  const logout = () => {
    setAuth((prevAuth) => ({ ...prevAuth, loading: true })); // Set loading to true when logging out
    localStorage.removeItem("authData");
  };

  const authContextValue: AuthContextValue = {
    auth,
    setAuth: updateAuthData,
    logout, // Include the logout function in the context value
  };

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  );
};
