'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class coursesTaken extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  coursesTaken.init({
    courseName: {
      type: DataTypes.STRING,
      allowNull: false
    },
    duration: {
      type: DataTypes.STRING,
      allowNull: false
    },
    userDetailId: {
      type: DataTypes.INTEGER,
      references:{
        model:{
          tableName: "userDetails"
        },
        key: "id"
      },
      allowNull: false
    }
  }, {
    sequelize,
    modelName: 'coursesTaken',
  });

  coursesTaken.associate = function(models){
    coursesTaken.belongsTo(models.userDetails);
  }
  return coursesTaken;
};