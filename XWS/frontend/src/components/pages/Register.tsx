import { useState } from "react";
import { Link } from "react-router-dom";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import "react-toastify/dist/ReactToastify.css";
import useAuthForm from "../../hooks/useAuthForm";

const Register = () => {
  const { handleRegister } = useAuthForm();

  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [matchPwd, setMatchPwd] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [address, setAddress] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    handleRegister(
      email,
      username,
      password,
      matchPwd,
      first_name,
      last_name,
      address
    );
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
    <>
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
    </>
  );
};

export default Register;
