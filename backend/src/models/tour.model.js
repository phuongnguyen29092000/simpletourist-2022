const mongoose = require("mongoose");

const tourSchema = mongoose.Schema(
  {
    tourName: {
      type: String,
      minlength: 0,
      maxlength: 100,
      required: [true, "Tour must have a name!"],
    },
    countryName: {
      type: String,
      minlength: 0,
      maxlength: 100,
      required: [true, "Tour must have a country name!"],
    },
    continent: {
      type: String,
      enum: ["Asia", "Europe"],
    },
    description: {
      type: String,
      minlength: 20,
      maxlength: 1024,
      required: [true, "Tour must have a description!"],
    },
    imageAvatar: {
      type: String,
      maxlength: 500,
      trim: true,
    },
    imageSlide: {
      type: [String],
    },
    price: {
      type: Number,
      require: true,
      min: 0,
      max: 100000000,
    },
    timeStart: {
      type: Date,
      required: [true, "Tour must have a time start!"],
    },
    timeEnd: {
      type: Date,
      required: [true, "Tour must have a time end!"],
    },
    amount: {
      type: Number,
      required: true,
      min: 0,
      max: 50,
      default: 50,
    },
    hotelName: {
      type: String,
      minlength: 0,
      maxlength: 100,
      required: [true, "Tour must have a hotel name!"],
    },
    schedule: {
      type: String,
      minlength: 20,
      maxlength: 1024,
      required: true,
    },
    typePlace: {
      type: mongoose.Schema.ObjectId,
      ref: "TypePlace",
    },
    discount: {
      type: Number,
      default: 0,
    },
    owner: {
      type: mongoose.Schema.ObjectId,
      ref: "User",
      required: [true, "Tour must belong to owner!"],
    },
  },
  {
    timestamps: true,
  }
);

const Tour = mongoose.model("Tour", tourSchema);

module.exports = Tour;
