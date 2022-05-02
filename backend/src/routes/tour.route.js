const express = require("express");

const { TourController } = require("../controllers");
const { uploadAvatar, uploadImageSlide } = require('../middlewares/fileUpload')

const router = express.Router();

router
    .route("/")
    .get(TourController.getAllTour)
    .post(TourController.createTour);

router
    .route("/:id")
    .get(TourController.getTour)
    .delete(TourController.deleteTour)
    .patch(TourController.updateTour);

module.exports = router;