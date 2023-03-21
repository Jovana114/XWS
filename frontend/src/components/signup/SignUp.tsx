import React, { FC } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import axios from "axios";
import { ToastContainer, toast, Flip } from "react-toastify";
import AuthWrapper from "../wrapper/AuthWrapper";

const SignUp: React.FC = () => {
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    watch,
    reset,
    formState: { errors },
  } = useForm();
  const submitData = (data: any) => {
    let params = {
      username: data.username,
      email: data.email,
      password: data.password,
      confirmpassword: data.cpassword,
    };
    console.log(data);
    axios
      .post("http://localhost:8080/api/auth/signup", params)
      .then(function (response) {
        toast.success(response.data.message, {
          position: "top-right",
          autoClose: 3000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: false,
          progress: 0,
          toastId: "my_toast",
        });
        reset();
        setTimeout(() => {
          navigate("/login");
        }, 3000);
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
          <div className="card mb-3 mt-3 rounded" style={{ width: "100%" }}>
            <div className="col-md-12">
              <div className="card-body" style={{height: '450px'}}>
                <h3 className="card-title text-center text-secondary mt-3 mb-3">
                  Sign Up Form
                </h3>
                <form
                  className="row"
                  autoComplete="off"
                  onSubmit={handleSubmit(submitData)}
                >
                  <div className="col-md-6">
                    <div className="">
                      <label className="form-label">Username</label>
                      <input
                        type="text"
                        className="form-control form-control-sm"
                        id="exampleFormControlInput1"
                        {...register("username", {
                          required: "Username is required!",
                        })}
                      />
                      
                    </div>
                  </div>
                  

                  <div className="">
                    <label className="form-label">Email</label>
                    <input
                      type="email"
                      className="form-control form-control-sm"
                      id="exampleFormControlInput3"
                      {...register("email", { required: "Email is required!" })}
                    />
                  </div>
                  <div className="">
                    <label className="form-label">Password</label>
                    <input
                      type="password"
                      className="form-control form-control-sm"
                      id="exampleFormControlInput5"
                      {...register("password", {
                        required: "Password is required!",
                      })}
                    />
                  </div>
                  <div>
                    <label className="form-label">Confirm Password</label>
                    <input
                      type="password"
                      className="form-control form-control-sm"
                      id="exampleFormControlInput6"
                      {...register("cpassword", {
                        required: "Confirm Password is required",

                        validate: (value) =>
                          value === watch("password") ||
                          "Passwords don't match.",
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
                    <p className="card-text">
                      Already have an account?{" "}
                      <Link style={{ textDecoration: "none" }} to={"/login"}>
                        Log In
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

export default SignUp;
