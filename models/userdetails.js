'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class userDetails extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  userDetails.init({
    name: DataTypes.STRING,
    phoneNo: DataTypes.STRING
  }, {
    sequelize,
    modelName: 'userDetails',
  });

  userDetails.associate = function(models){
    userDetails.hasOne(models.credentials);
    userDetails.hasMany(models.coursesTaken, {foreignKey: 'userDetailId'});
  }

  return userDetails;
};