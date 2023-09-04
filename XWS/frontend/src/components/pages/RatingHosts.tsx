import { useState, useEffect } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  TextField,
  Typography,
} from "@mui/material";
import useAuth from "../../hooks/useAuth";
import useRating from "../../hooks/useRating";

const RatingHost = () => {
  const { user } = useAuth();
  const { createRating, updateRating, deleteRating, getRatingsByHost } = useRating();

  const [hostId, setHostId] = useState<string>("");
  const [rating, setRating] = useState<number>(0);
  const [ratings, setRatings] = useState<any[]>([]);
  const [openDialog, setOpenDialog] = useState<boolean>(false);

  useEffect(() => {
    if (user && user.hasPreviousReservation) {
      getRatingsByHost(hostId).then((data: any[]) => {
        setRatings(data);
      });
    }
  }, [user, hostId, getRatingsByHost]);

  const handleOpenDialog = (hostId: string) => {
    setHostId(hostId);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setRating(0);
    setHostId("");
  };

  const handleRatingSubmit = async () => {
    try {
      if (rating > 0) {
        const existingRating = ratings.find((r) => r.guestId === user.id);
        if (existingRating) {
          await updateRating(existingRating.id, rating);
        } else {
          await createRating(hostId, { // Remove the third argument here
            startDate: "", // Replace with the actual start date
            endDate: "",   // Replace with the actual end date
            numGuests: 0,  // Replace with the actual number of guests
          });
        }
  
        const updatedRatings = await getRatingsByHost(hostId);
        setRatings(updatedRatings);
  
        handleCloseDialog();
      }
    } catch (error) {
      // Handle errors as needed
    }
  };
  const handleDeleteRating = async (ratingId: any) => {
    try {
      await deleteRating(ratingId);

      const updatedRatings = await getRatingsByHost(hostId);
      setRatings(updatedRatings);
    } catch (error) {
      // Handle errors as needed
    }
  };

  function formatDateTime(date: any): import("react").ReactNode {
    throw new Error("Function not implemented.");
  }

  return (
    <>
      <div>
        <Typography variant="h5">Ratings for Host</Typography>
        <ul>
          {ratings.map((rating) => (
            <li key={rating.id}>
              <span>Rating: {rating.value}</span>
              <span>Given by: {rating.guestId}</span>
              <span>Date: {formatDateTime(rating.date)}</span>
              <Button
                onClick={() => handleDeleteRating(rating.id)}
                variant="outlined"
                color="secondary"
              >
                Delete
              </Button>
            </li>
          ))}
        </ul>
      </div>

      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        aria-labelledby="rating-dialog-title"
        aria-describedby="rating-dialog-description"
      >
        <DialogTitle id="rating-dialog-title">Rate Host</DialogTitle>
        <DialogContent>
          <FormControl>
            <TextField
              type="number"
              label="Rating (1-5)"
              value={rating}
              onChange={(e) => setRating(Number(e.target.value))}
              inputProps={{ min: 1, max: 5 }}
            />
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            Cancel
          </Button>
          <Button onClick={handleRatingSubmit} color="primary">
            Submit Rating
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default RatingHost;
