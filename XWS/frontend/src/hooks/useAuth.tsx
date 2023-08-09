import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";

const useAuth = () => {
  const { auth }: any = useContext(AuthContext);
  return auth;
};

export default useAuth;
