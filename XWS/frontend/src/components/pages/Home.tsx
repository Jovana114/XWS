import Guest from "./Guest";
import Host from "./Host";
import useAuth from "../../hooks/useAuth";

document.title = "Home";

const Home = () => {
  const auth = useAuth();

  return (
    <section>
      <h1>Home</h1>
      <br />
      {auth.roles?.includes("ROLE_HOST") ? <Host /> : <Guest />}
      <br />
      <p>You are logged in!</p>
      <br />
    </section>
  );
};

export default Home;
