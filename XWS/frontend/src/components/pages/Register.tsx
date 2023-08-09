import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axiosPrivate from "../../api/axios";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { ThemeProvider } from "@emotion/react";
import theme from "../../style/theme";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";

import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const USER_REGEX = /^[A-z][A-z0-9-_]{3,23}$/;
const EMAIL_REGEX = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
const REGISTER_URL = "/auth/signup";

const Register = () => {
  document.title = "Sign up";
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [matchPwd, setMatchPwd] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [address, setAddress] = useState("");
  const [errMsg, setErrMsg] = useState("");

  const validateForm = () => {
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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // Validate the form
    if (!validateForm()) {
      toast.error("Please fill out all the required fields.");
      return;
    }

    const v1 = USER_REGEX.test(username);
    const v2 = true; // Password validation removed
    const v3 = EMAIL_REGEX.test(email);
    if (!v1 || !v2 || !v3) {
      toast.error("Invalid Entry");
      return;
    }

    try {
      await axiosPrivate.post(
        REGISTER_URL,
        {
          email,
          username,
          password,
          first_name,
          last_name,
          address,
        },
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      // Clear form fields and display success toast
      setUsername("");
      setPassword("");
      setMatchPwd("");
      setFirstName("");
      setLastName("");
      setAddress("");
      setEmail("");
      toast.success("Registration Successful!");
      navigate("/signin"); // Redirect to sign-in page after successful registration
    } catch (err: any) {
      // Handle server errors and display error toast
      if (!err.response) {
        setErrMsg("No Server Response");
      } else {
        setErrMsg("Registration Failed!");
      }
      toast.error(errMsg);
    }
  };

  const isFormValid =
    email !== "" &&
    username !== "" &&
    password !== "" &&
    matchPwd &&
    address !== "" &&
    first_name !== "" &&
    last_name !== "";

  return (
    <ThemeProvider theme={theme}>
      <section>
        <Typography variant="h4">Register</Typography>
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
          <FormControl variant="outlined">
            <TextField
              type="email"
              id="email"
              label="Email"
              value={email}
              required
              onChange={(e) => setEmail(e.target.value)}
              autoComplete="off"
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="text"
              id="username"
              label="Username"
              autoComplete="off"
              value={username}
              required
              onChange={(e) => setUsername(e.target.value)}
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="password"
              id="password"
              label="Password"
              value={password}
              required
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="off"
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="password"
              id="confirm_pwd"
              label="Confirm Password"
              value={matchPwd}
              required
              onChange={(e) => setMatchPwd(e.target.value)}
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="text"
              id="first_name"
              label="First Name"
              value={first_name}
              required
              onChange={(e) => setFirstName(e.target.value)}
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="text"
              id="last_name"
              label="Last Name"
              value={last_name}
              required
              onChange={(e) => setLastName(e.target.value)}
            />
          </FormControl>

          <FormControl variant="outlined">
            <TextField
              type="text"
              id="address"
              label="Address"
              value={address}
              required
              onChange={(e) => setAddress(e.target.value)}
            />
          </FormControl>

          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={!isFormValid}
          >
            Sign Up
          </Button>
        </Box>
        <Typography variant="body1">
          Already registered?
          <br />
          <Link to="/signin">
            <Button type="submit" variant="contained" color="primary">
              Sign in
            </Button>
          </Link>
        </Typography>
      </section>
      <ToastContainer />
    </ThemeProvider>
  );
};

export default Register;
