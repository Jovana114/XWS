import { useState } from "react";
import { Link } from "react-router-dom";

import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import "react-toastify/dist/ReactToastify.css";
import { VisibilityOff, Visibility } from "@mui/icons-material";
import {
  FormControl,
  InputLabel,
  OutlinedInput,
  InputAdornment,
  IconButton,
} from "@mui/material";
import useAuthForm from "../../hooks/useAuthForm";

const Login = () => {
  const { handleLogin } = useAuthForm();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleClickShowPassword = () => {
    setShowPassword((show) => !show);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    handleLogin(username, password);
  };

  return (
    <>
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
          autoComplete="off"
          value={username}
          required
          onChange={(e) => setUsername(e.target.value)}
        />
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
        </FormControl>
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
    </>
  );
};

export default Login;
