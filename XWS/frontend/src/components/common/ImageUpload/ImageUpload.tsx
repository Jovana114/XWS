/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";
import { Button } from "@mui/material";
import { withStyles } from "@mui/styles";
import Card from "@mui/material/Card";
import Grid from "@mui/material/Grid";

// Your other imports...

// Define the styles for the image upload card
const styles = () => ({
  root: {
    margin: "8px 0",
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-end",
  },
  input: {
    display: "none",
  },
  img: {
    width: "300px",
    height: "200px",
    margin: "auto",
    display: "block",
    maxWidth: "100%",
    maxHeight: "100%",
  },
});

const ImageUploadCard = ({ classes }: any) => {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleUploadClick = (event: any) => {
    const file = event.target.files[0];
    setSelectedFile(file);
  };

  const renderImageUploadCard = () => {
    return (
      <Grid container direction="column" alignItems="center">
        <Grid item>
          <img
            width="100%"
            className={classes.img}
            style={{
              objectFit: "contain",
            }}
            src={
              selectedFile
                ? URL.createObjectURL(selectedFile)
                : "https://placehold.co/600x400"
            }
            alt="Selected"
          />
        </Grid>
        <label htmlFor="contained-button-file">
          <Button variant="contained" component="span">
            Select Image
            <input
              accept="image/*"
              className={classes.input}
              id="contained-button-file"
              type="file"
              onChange={handleUploadClick}
            />
          </Button>
        </label>
      </Grid>
    );
  };

  return (
    <div className={classes.root}>
      <Card>{renderImageUploadCard()}</Card>
    </div>
  );
};

const ImageUpload = withStyles(styles)(ImageUploadCard);

export default ImageUpload;
