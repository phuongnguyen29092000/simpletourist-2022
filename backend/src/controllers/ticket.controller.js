const httpStatus = require('http-status');
const validator = require('validator')
const catchAsync = require('../utils/catchAsync')
const { ticketService, userService } = require('../services')

const bookTicket = catchAsync(async(req, res) => {
    console.log(req.params.tourId);
    const ticket = await ticketService.bookTicket(req.params.tourId, req.body)

    if (!ticket) res.status(httpStatus.BAD_REQUEST).json({
        status: 400,
        message: "Đặt vé không thành công!",
    })

    res.status(httpStatus.CREATED).json({
        status: 201,
        message: "Đặt vé thành công!",
        ticket: ticket
    })
})

const getAllTicket = catchAsync(async(req, res) => {
    const perPage = 6;
    let page = parseInt(req.query.page) || 1;
    const tickets = await ticketService.getAllTicket(page, perPage)

    if (tickets.length == 0) res.status(httpStatus.NOT_FOUND).json({
        status: 404,
        message: "Không tìm thấy vé",
    })

    res.status(httpStatus.OK).json({
        status: 200,
        message: "OK",
        tickets: tickets
    })
})

const getTicketById = catchAsync(async(req, res) => {
    const ticket = await ticketService.getTicketById(req.params.id)

    if (!tourData) res.status(httpStatus.NOT_FOUND).json({
        status: 404,
        message: "Không tìm thấy vé"
    })

    res.status(httpStatus.OK).json({
        status: 200,
        message: "OK",
        ticket: ticket
    })
})

const getTicketPerTour = catchAsync(async(req, res) => {
    const tickets = await ticketService.getTicketPerTour(req.params.idTour)

    if (tickets.length == 0) res.status(httpStatus.NOT_FOUND).json({
        status: 404,
        message: "Không tìm thấy vé"
    })

    res.status(httpStatus.OK).json({
        status: 200,
        message: "OK",
        tickets: tickets
    })
})

const updateTicketById = catchAsync(async(req, res) => {
    const ticket = await ticketService.updateTicketById(req.params.id, req.body)

    if (!ticket) res.status(httpStatus.BAD_REQUEST).json({
        status: 400,
        message: "Cập nhật vé không thành công!"
    })

    res.status(httpStatus.OK).json({
        status: 200,
        message: 'OK',
        ticket: ticket
    })
})

const deleteTicketById = catchAsync(async(req, res) => {
    await ticketService.deleteTicketById(req.params.id)
    res.status(httpStatus.NO_CONTENT).send({
        status: 204,
        message: "Xóa thành công!"
    })
})

module.exports = {
    bookTicket,
    getAllTicket,
    getTicketById,
    updateTicketById,
    deleteTicketById,
    getTicketPerTour
}