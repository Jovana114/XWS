import { useEffect } from "react";
import useRating from "../../hooks/useRating";

const RatingHosts = () => {
  const { hostRatingData, error, submitRating, getRating } = useRating(true);

  useEffect(() => {
    const hostId = "yourHostId";
    getRating(hostId);
  }, []);

  const handleRatingSubmit = (hostId: string, rating: number, review: string) => {
    submitRating(hostId, rating, review);
  };


  return (
    <div>
      <h1>Rating Hosts</h1>
      {error && <div className="error-message">{error}</div>}
      <br></br>
      <div>
        {/* Display host rating data */}
        <h2>Host Rating Data</h2>
        <pre>{JSON.stringify(hostRatingData, null, 2)}</pre>
      </div>
      <br></br>
      <div>
        {/* Rating form */}
        <h2>Submit Rating</h2>
        <br></br>
        <form onSubmit={(e) => e.preventDefault()}>
          <input type="text" placeholder="Reservation ID" />
          <input type="text" placeholder="Host ID" />
          <input type="number" placeholder="Rating" />
          <button onClick={() => handleRatingSubmit("yourHostId", 5, "Great host!")}>
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default RatingHosts;
