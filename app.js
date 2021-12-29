const express = require("express");
const CredentialModel = require("./models").credentials;
const UserModel = require("./models").userDetails;
const CoursesTakenModel = require("./models").coursesTaken;

const app = express();
const PORT = 8087;

app.get("/", (req, res) => {
    res.status(200).json({
        status: 1,
        message: "welcome to homepage"
    });
});

// to get all users
app.get("/users", (req, res) => {
    UserModel.findAll({
        include: CredentialModel,
        where: {id: 2}
    }).then((data) => {
        res.status(200).json({
            status: 1,
            data: data
        })
        
    }).catch((err) => {
        res.status(500).json({
            status: 0,
            message: "we got error"
        })
        console.log("we got an error", err);
    })
});

// to get all credentials
app.get("/credentials", (req, res) => {
    CredentialModel.findAll({
        include:UserModel,
        where: {id: 2}
    }).then((data) => {
        res.status(200).json({
            status: 1,
            data: data
        })
    }).catch((err) => {
        res.status(500).json({
            status: 0,
            message: "we got error"
        })
        console.log("error", err);
    })
});

//to get all courses taken
app.get("/courses", (req, res) => {
    CoursesTakenModel.findAll({
        include:UserModel,
        where: {userDetailId: 1}
    }).then((data) => {
        res.status(200).json({
            status: 1,
            data: data
        })
    }).catch((err) => {
        res.status(500).json({
            status: 0,
            message: "we got error"
        })
        console.log("error", err);
    })
});


app.listen(PORT, ()=>{
    console.log("application is listening at: "+ PORT);
})
