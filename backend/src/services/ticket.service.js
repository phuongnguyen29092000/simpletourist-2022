const { Ticket, Tour } = require('../models')

const bookTicket = async(tour, ticketBody) => {
    console.log(tour, ticketBody);
    const ticket = await Ticket.create({...ticketBody, tour })
    return ticket
}

const getAllTicket = async(page, perPage) => {
    const tickets = await Ticket.find()
        // .skip((perPage * page) - perPage)
        // .limit(perPage)

    return tickets
}

const getTicketPerTour = async(idTour) => {
    let ticketPerTour = []
    const tickets = await Ticket.find().populate({ path: 'tour' })
    tickets.forEach(ticket => {
        if (ticket.tour._id == idTour) {
            ticketPerTour.push({
                id: ticket._id,
                cusomterId: ticket.customer,
                ownerId: ticket.owner,
                phone: ticket.phone,
                tourName: ticket.tour.tourName,
                totalPrice: parseInt(ticket.paymentPrice * ticket.numberPeople),
                numberPeople: ticket.numberPeople,
                status: ticket.status,
                createdAt: ticket.createdAt,
                updatedAt: ticket.updatedAt
            })
        }
    });
    return ticketPerTour
}

const getTicketById = async(id) => {
    const ticket = await Ticket.findById(id)
    return ticket
}

const updateTicketById = async(id, ticketBody) => {
    const ticket = await getTicketById(id)
    Object.assign(ticket, ticketBody);
    await ticket.save();
    return ticket;
}

const deleteTicketById = async(id) => {
    const ticket = await getTicketById(id)
    await ticket.remove()
    return ticket
}

module.exports = {
    bookTicket,
    getAllTicket,
    getTicketById,
    updateTicketById,
    deleteTicketById,
    getTicketPerTour
}