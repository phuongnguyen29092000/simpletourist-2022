const catchAsync = require('../utils/catchAsync');
const httpStatus = require('http-status');
const { typePlaceService } = require('../services')

/* create new type place */
const createTypePlace = catchAsync(async(req, res) => {
    // console.log(req.body);
    const typePlace = await typePlaceService.createTypePlace(req.body)
    res.status(httpStatus.CREATED).send(typePlace)
})

/* get all type place */
const getAllTypePlace = catchAsync(async(req, res) => {
    const typePlaces = await typePlaceService.getAllTypePlace()
    if (!typePlaces) {
        res.status(httpStatus.NOT_FOUND).send("Type place not found")
    } else res.status(200).send(typePlaces)
})


/* get typePlace detail by params id */
const getTypePlaceById = catchAsync(async(req, res) => {
    const typePlace = await typePlaceService.getTypePlaceById(req.params.id)

    if (!typePlace) {
        res.status(httpStatus.NOT_FOUND).send("Type place not found")
    } else res.send(typePlace);
})

/* update type place detail by params id*/
const updateTypePlacesById = catchAsync(async(req, res) => {
    const typePlace = await typePlaceService.updateTypePlaceById(
        req.params.id,
        req.body
    )

    res.status(200).send(typePlace)
})


/* delete type place by params id */
const deleteTypePlaceById = catchAsync(async(req, res) => {
    await typePlaceService.deleteTypePlaceById(req.params.id)
    res.status(httpStatus.NO_CONTENT).send()
})


module.exports = {
    createTypePlace,
    getAllTypePlace,
    getTypePlaceById,
    updateTypePlacesById,
    deleteTypePlaceById
}