import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, TextField } from "@mui/material";
import useAccomodation from "../../hooks/useAccommodation";
import DoubleTable from "../common/Table/DoubleTable";
import { useState } from "react";

const AppointmentReservation = () => {
  const { data, fetchFilteredAccommodationData } = useAccomodation(false);
  const [location, setLocation] = useState("");
  const [numGuests, setNumGuests] = useState(0);
  const [start, setStart] = useState<string>(new Date().toISOString());
  const [end, setEnd] = useState<string>(new Date().toISOString());

  const [openDialog, setOpenDialog] = useState(false);

  const handleDialogSubmit = (e: any) => {
    // Implement your account deletion logic here
    // This function will be triggered when "Confirm Delete" button is clicked
    // You can also close the dialog here
    console.log('====================================');
    console.log(e);
    console.log('====================================');
    setOpenDialog(false);
  };

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const columns = [
    { key: "benefits", text: "Benefits" },
    { key: "location", text: "Location" },
    { key: "max_guests", text: "Max number of guests" },
    { key: "min_guests", text: "Min number of guests" },
    { key: "name", text: "Name" },
  ];
  const collapseColumns = [
    { key: "start", text: "Start Date" },
    { key: "end", text: "End Date" },
    { key: "price", text: "Price"},
    { key: "price_per", text: "Price For"},
    { key: "id", text: "", label: "Reserve"}
  ];

  const handleFilters = async () => {
    fetchFilteredAccommodationData(
      location,
      numGuests,
      formatDateTime(start),
      formatDateTime(end)
    );
  };
  const disableForm = location !== "" && numGuests > 0 && start && end;

  const formatDateTime = (dateTimeString: string): string => {
    const date = new Date(dateTimeString);
    return date.toISOString().slice(0, 19);
  };

  return (
    <>
          <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirm Account Deletion"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to delete your account? This action is
            irreversible and will delete all your data permanently.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          {/* <Button onClick={handleDelete} variant="contained" color="primary">
            Confirm Delete
          </Button> */}
        </DialogActions>
      </Dialog>
      <div>
        <FormControl
          variant="outlined"
          style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
          }}
        >
          <TextField
            style={{ margin: "0 10px" }}
            type="text"
            id="location"
            label="Location"
            value={location}
            required
            onChange={(e) => setLocation(e.target.value)}
          />
          <TextField
            style={{ margin: "0 10px", width: "150px" }}
            type="number"
            id="numGuests"
            label="Number of Guests"
            value={numGuests}
            required
            onChange={(e) => setNumGuests(parseInt(e.target.value))}
          />
          <TextField
            style={{ margin: "0 10px" }}
            type="datetime-local"
            id="start"
            label="Arriving Date"
            value={start}
            required
            onChange={(e) => setStart(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            style={{ margin: "0 10px" }}
            type="datetime-local"
            id="end"
            label="Departure Date"
            value={end}
            required
            onChange={(e) => setEnd(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
          />

          <Button disabled={!disableForm} onClick={handleFilters}>
            Filter Search
          </Button>
        </FormControl>
      </div>
      <DoubleTable
        data={data}
        columns={columns}
        collapseColumn="appointments"
        collapseColumns={collapseColumns}
        onButtonClick={handleOpenDialog}
      />
    </>
  );
};

export default AppointmentReservation;
