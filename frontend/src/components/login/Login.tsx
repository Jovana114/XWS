import React, { FC } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { ToastContainer, toast, Flip } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from "react-router-dom";
import AuthWrapper from "../wrapper/AuthWrapper";

const Login: React.FC = (): JSX.Element => {
  
  document.title = "Login"

  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const login = (data: any) => {
    let params = {
      username: data.username,
      password: data.password,
    };
    axios
      .post("http://localhost:8080/api/auth/signin", params)
      .then(function (response) {
        //   IF EMAIL ALREADY EXISTS
        if (response.data.success === false) {
          toast.error('Login Failed!', {
            position: toast.POSITION.TOP_RIGHT
        });
        } else {
          toast.success('Login Successful!', {
            position: toast.POSITION.TOP_RIGHT
        });
          localStorage.setItem("token", response.data.accessToken);
          setTimeout(() => {
            navigate("/home");
          }, 3000);
        }
      })

      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <AuthWrapper>
      <div className="container">
        <div
          className="row d-flex justify-content-center align-items-center"
        >
          <div className="card mb-3" style={{ width: "100%" }}>
            <div className="col-md-12">
              <div className="card-body" style={{height: '450px'}}>
                <h3 className="card-title text-center text-secondary mt-3">
                  Login Form
                </h3>
                <form autoComplete="off" onSubmit={handleSubmit(login)}>
                  <div className="">
                    <label className="form-label">Username</label>
                    <input
                      type="text"
                      className="form-control form-control-sm"
                      id="exampleFormControlInput1"
                      {...register("username", { required: "username is required!" })}
                    />
                    {/* {errors.
                     && (
                      <p className="text-danger" style={{ fontSize: 14 }}>
                        {errors.email.message}
                      </p>
                    )} */}
                  </div>
                  <div className="">
                    <label className="form-label">Password</label>
                    <input
                      type="password"
                      className="form-control form-control-sm"
                      id="exampleFormControlInput2"
                      {...register("password", {
                        required: "Password is required!",
                      })}
                    />
                  </div>
                  <div className="text-center mt-4 ">
                    <button
                      className="btn btn-outline-primary text-center shadow-none mb-3"
                      type="submit"
                    >
                      Submit
                    </button>
                    <p className="card-text pb-2">
                      Don't have an Account?{" "}
                      <Link style={{ textDecoration: "none" }} to={"/register"}>
                        Sign Up
                      </Link>
                    </p>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar
        closeOnClick
        rtl={false}
        pauseOnFocusLoss={false}
        draggable={false}
        pauseOnHover
        limit={1}
        transition={Flip}
      />
    </AuthWrapper>
  );
};
export default Login;
