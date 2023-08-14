import Guest from "./Guest";
import Host from "./Host";
import useAuth from "../../hooks/useAuth";

document.title = "Home";

const Home = () => {
  const auth = useAuth();

  return <>{auth.auth.roles?.includes("ROLE_HOST") ? <Host /> : <Guest />}</>;
};

export default Home;
