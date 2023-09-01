import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosPrivate from "../api/axios";
import { AuthContext } from "../auth/AuthContext";
import { toast } from "react-toastify";
import {
  LOGIN_URL,
  USER_REGEX,
  EMAIL_REGEX,
  REGISTER_URL,
} from "../constants/contsnts";

interface LoginResponseData {
  id: string;
  email: string;
  username: string;
  accessToken: string;
  roles: string[];
}

const useAuthForm = () => {
  const { auth, setAuth } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (auth.accessToken) {
      navigate("/"); // Redirect to "/home" if access token exists
    }
  }, [auth.accessToken, navigate]);

  const validateRegisterForm = (
    email: string,
    username: string,
    password: string,
    matchPwd: string,
    first_name: string,
    last_name: string,
    address: string
  ) => {
    return (
      email.trim() !== "" &&
      username.trim() !== "" &&
      password.trim() !== "" &&
      matchPwd.trim() !== "" &&
      first_name.trim() !== "" &&
      last_name.trim() !== "" &&
      address.trim() !== ""
    );
  };

  const handleLogin = async (_username: string, password: string) => {
    try {
      const response = await axiosPrivate.post<LoginResponseData>(LOGIN_URL, {
        username: _username,
        password,
      });

      const { id, email, username, accessToken, roles } = response.data;

      // Save specific data to local storage
      const authData = {
        id,
        email,
        accessToken,
        username: username,
        roles, // Assign the extracted role
        loading: false,
      };
      localStorage.setItem("authData", JSON.stringify(authData));

      setAuth({ id, email, username, accessToken, roles });

      navigate("/", { replace: true });
    } catch (err: any) {
      if (!err.response) {
        toast.error("No Server Response");
      } else if (err.response.status === 400) {
        toast.error("Missing Username or Password");
      } else if (err.response.status === 401) {
        toast.error("Unauthorized");
      } else {
        toast.error("Login Failed");
      }
    }
  };

  const handleRegister = async (
    email: string,
    username: string,
    password: string,
    matchPwd: string,
    first_name: string,
    last_name: string,
    address: string,
    isHost: boolean
  ) => {
    try {
      if (
        !validateRegisterForm(
          email,
          username,
          password,
          matchPwd,
          first_name,
          last_name,
          address
        )
      ) {
        toast.error("Please fill out all the required fields.");
        return;
      }

      const v1 = USER_REGEX.test(username);
      const v2 = EMAIL_REGEX.test(email);
      if (!v1 || !v2) {
        toast.error("Invalid Entry");
        return;
      }
      const roles = isHost ? ["host"] : null;
      await axiosPrivate.post(REGISTER_URL, {
        email,
        username,
        password,
        first_name,
        last_name,
        address,
        roles,
      });

      navigate("/signin");
      toast.success("Registration Successful!");
    } catch (err: any) {
      if (!err.response) {
        toast.error(err);
      }
    }
  };

  return { handleLogin, handleRegister };
};

export default useAuthForm;
