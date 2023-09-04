import React, { useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  TextField,
} from "@mui/material";
import DoubleTable from "../common/Table/DoubleTable";
import useFilter from "../../hooks/useFilter";

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
  { key: "priceRatingMax", text: "Price" },
  { key: "price_per", text: "Price For" },
  { key: "id", text: "", label: "Reserve" },
];

const AccommodationFilter = () => {
  const { data, findByPrice, findByBenefits, findHighlightedHosts } = useFilter(false);

  
  const [benefits, setBenefits] = useState<Text>(new Text(""));
  const [priceForMin, setPriceForMin] = useState(0);
  const [priceForMax, setPriceForMax] = useState(0);

  const [openDialog, setOpenDialog] = useState(false);

  const [benefitsFor, setBenefitsFor] = useState("");
  const [priceMin, setPriceMin] = useState<number>(2);
  const [priceMax, setPriceMax] = useState<number>(3);

  const findByPriceR = (priceMinR: number, priceMaxR: number) => {
    setPriceMin(priceMinR);
    setPriceMax(priceMaxR);
    findByPrice(priceMinR, priceMaxR);
  };

  const handleOpenDialog = (e: any) => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  return (
    <>
      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Accommodation filter"}</DialogTitle>
        <DialogContent
          style={{
            display: "flex",
            flexDirection: "column",
            padding: "20px",
          }}
        >
          <TextField
            style={{ margin: "0 10px" }}
            type="text"
            id="benefits"
            label="Benefits"
            value={benefitsFor}
            required
            onChange={(e) => setBenefitsFor(e.target.value)}
          />
          <TextField
            style={{ margin: "10px 0" }}
            type="number"
            id="priceRatingMin"
            label="Price min"
            value={priceMin}
            required
            onChange={(e) => setPriceMin(parseInt(e.target.value))}
          />
          <TextField
            style={{ margin: "10px 0" }}
            type="number"
            id="priceRatingMax"
            label="Price max"
            value={priceMin}
            required
            onChange={(e) => setPriceMax(parseInt(e.target.value))}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
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
            id="benefits"
            label="Benefits"
            value={benefitsFor}
            required
            onChange={(e) => setBenefitsFor(e.target.value)}
          />
          <TextField
            style={{ margin: "10px 0" }}
            type="number"
            id="priceRatingMin"
            label="Price min"
            value={priceMin}
            required
            onChange={(e) => setPriceMin(parseInt(e.target.value))}
          />
          <TextField
            style={{ margin: "10px 0" }}
            type="number"
            id="priceRatingMax"
            label="Price max"
            value={priceMax}
            required
            onChange={(e) => setPriceMax(parseInt(e.target.value))}
          />
        </FormControl>
      </div>

      <div>
        <br />
        <Button onClick={() => findByPriceR(priceForMin, priceForMax)}>
          Find by price rating
        </Button>
        <Button onClick={() => findByBenefits(benefits)}>
          Find by benefits
        </Button>
        <Button onClick={() => findHighlightedHosts()}>
          Find highlighted hosts
        </Button>
      </div>

      <DoubleTable
        data={data}
        columns={columns}
        collapseColumn="accommodations"
        collapseColumns={collapseColumns}
        onColumnButtonClick={(e) => handleOpenDialog(e)}
      />
    </>
  );
};

export default AccommodationFilter;
