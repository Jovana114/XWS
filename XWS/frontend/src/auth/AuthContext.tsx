/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-empty-function */
import React, {
  createContext,
  useState,
  useEffect,
  SetStateAction,
} from "react";
import Loader from "../components/common/Loader/Loader";

export interface AuthData {
  id?: string;
  email?: string;
  username?: string;
  accessToken?: string;
  roles?: string[];
}

export interface AuthContextValue {
  auth: AuthData;
  loading: boolean;
  setLoading: (data: boolean) => void;
  setAuth: React.Dispatch<SetStateAction<AuthData>>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue>({
  auth: {},
  loading: true,
  setLoading: () => {},
  setAuth: () => {},
  logout: () => {},
});

type AuthProps = {
  children: React.ReactNode;
};

export const AuthProvider = (props: AuthProps) => {
  const storedAuthData = localStorage.getItem("authData");
  const initialAuthData: AuthData = storedAuthData
    ? JSON.parse(storedAuthData)
    : {};

  const [auth, setAuth] = useState<AuthData>({
    ...initialAuthData,
  });
  const [loading, setLoading] = useState<boolean>(!initialAuthData.accessToken);

  useEffect(() => {
    // Perform additional validation or expiration checks if needed
    if (!storedAuthData) {
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

  const updateLoading = (data: boolean) => {
    setLoading(data);
  };

  const logout = () => {
    setAuth({});
    setLoading(false);
    localStorage.removeItem("authData");
  };

  const authContextValue: AuthContextValue = {
    auth,
    loading,
    setLoading: updateLoading,
    setAuth: updateAuthData,
    logout,
  };

  return (
    <AuthContext.Provider value={authContextValue}>
      {!loading && props.children}
      {/* Only render the children when auth.loading is false */}
      {loading && <Loader />}
      {/* Render the Loader component when loading is true */}
    </AuthContext.Provider>
  );
};
