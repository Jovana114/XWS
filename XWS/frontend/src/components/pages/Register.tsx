import { useState } from "react";
import { Link } from "react-router-dom";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import "react-toastify/dist/ReactToastify.css";
import useAuthForm from "../../hooks/useAuthForm";
import { VisibilityOff, Visibility } from "@mui/icons-material";
import {
  InputLabel,
  OutlinedInput,
  InputAdornment,
  IconButton,
  Switch,
} from "@mui/material";

const Register = () => {
  const { handleRegister } = useAuthForm();

  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [matchPwd, setMatchPwd] = useState("");
  const [first_name, setFirstName] = useState("");
  const [last_name, setLastName] = useState("");
  const [address, setAddress] = useState("");
  const [isHost, setIsHost] = useState(false);

  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    handleRegister(
      email,
      username,
      password,
      matchPwd,
      first_name,
      last_name,
      address,
      isHost
    );
  };

  const handleClickShowPassword = () => {
    setShowPassword((show) => !show);
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
        <FormControl variant="outlined" sx={{ margin: "8px 0" }} required>
          <InputLabel htmlFor="password">Password</InputLabel>
          <OutlinedInput
            id="password"
            type={showPassword ? "text" : "password"}
            value={password}
            required
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="off"
            endAdornment={
              <InputAdornment position="end">
                <IconButton
                  aria-label="toggle password visibility"
                  onClick={handleClickShowPassword}
                  edge="end"
                >
                  {showPassword ? <VisibilityOff /> : <Visibility />}
                </IconButton>
              </InputAdornment>
            }
            label="Password"
          />
        </FormControl>{" "}
        <FormControl variant="outlined" sx={{ margin: "8px 0" }} required>
          <InputLabel htmlFor="password">Password</InputLabel>
          <OutlinedInput
            id="confirm_pwd"
            type={showPassword ? "text" : "password"}
            value={matchPwd}
            required
            onChange={(e) => setMatchPwd(e.target.value)}
            autoComplete="off"
            endAdornment={
              <InputAdornment position="end">
                <IconButton
                  aria-label="toggle password visibility"
                  onClick={handleClickShowPassword}
                  edge="end"
                >
                  {showPassword ? <VisibilityOff /> : <Visibility />}
                </IconButton>
              </InputAdornment>
            }
            label="Confirm Password"
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
        <FormControl
          style={{
            display: "flex",
            flexDirection: "row",
          }}
        >
          <Typography margin="auto 0">Sign up as Host</Typography>
          <Switch
            id="hostSwitch"
            checked={isHost}
            onChange={() => setIsHost((prev) => !prev)}
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
