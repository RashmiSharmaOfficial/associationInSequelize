'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class credentials extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  credentials.init({
    emailId: {
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
    modelName: 'credentials',
  });

  credentials.associate = function(models){
    credentials.belongsTo(models.userDetails);
  }

  return credentials;
};