import { useRef, useState, useEffect, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import axiosPrivate from "../../api/axios";
import { AuthContext } from "../../auth/AuthContext";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import { ThemeProvider } from "@emotion/react";
import theme from "../../style/theme";

import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const LOGIN_URL = "/auth/signin";

interface LoginResponseData {
  id: string;
  roles: string[];
  tokenType: string;
  accessToken: string;
}

const Login = () => {
  document.title = "Sign in";
  const { auth, setAuth } = useContext(AuthContext);
  const navigate = useNavigate();

  const userRef = useRef<HTMLInputElement>(null);
  const errRef = useRef<HTMLParagraphElement>(null);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errMsg, setErrMsg] = useState("");

  useEffect(() => {
    if (auth.accessToken) {
      navigate("/"); // Redirect to "/" if access token exists
    }
  }, [auth.accessToken, navigate]);

  useEffect(() => {
    setErrMsg("");
  }, [username, password]);

  // Assuming you already have the LoginResponseData interface defined

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await axiosPrivate.post<LoginResponseData>(
        LOGIN_URL,
        { username, password },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: false,
        }
      );

      const { accessToken, roles, id } = response.data;

      // Save specific data to local storage
      const authData = { id, roles, tokenType: "Bearer", accessToken };
      localStorage.setItem("authData", JSON.stringify(authData));

      setAuth({ accessToken, roles, id });

      setUsername("");
      setPassword("");

      navigate("/", { replace: true }); // Update the route to "/"
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

      if (errRef.current) {
        errRef.current.focus();
      }
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <section>
        <p
          ref={errRef}
          className={errMsg ? "errmsg" : "offscreen"}
          aria-live="assertive"
        >
          {errMsg}
        </p>
        <Typography variant="h4">Sign In</Typography>
        <Box
          component="form"
          sx={{
            display: "flex",
            flexDirection: "column",
            "& .MuiTextField-root": { my: 1, width: "300px" },
          }}
          noValidate
          autoComplete="off"
          onSubmit={handleSubmit}
        >
          <TextField
            type="text"
            id="username"
            label="Username"
            inputRef={userRef}
            autoComplete="off"
            value={username}
            required
            onChange={(e) => setUsername(e.target.value)}
          />

          <TextField
            type="password"
            id="password"
            label="Password"
            value={password}
            required
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button type="submit" variant="contained" color="primary">
            Sign In
          </Button>
        </Box>
        <Typography variant="body1">
          Need an Account?
          <br />
          <Link to="/signup">
            <Button type="submit" variant="contained" color="primary">
              Sign Up
            </Button>
          </Link>
        </Typography>
      </section>
      <ToastContainer />
    </ThemeProvider>
  );
};

export default Login;
