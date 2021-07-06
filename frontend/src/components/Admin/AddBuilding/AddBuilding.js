import React, { Component } from "react";
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import { backendURL } from "../../../utils/urlLinks";
import "./AddBuilding.css";
export default class AddBuilding extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modalIsOpen: false,
      isSendingAsyncRequest: false,
      success_asyncRequest: false,
      error_asyncRequest: false,
      buildingNameValue: "",
      buildingAddressValue: "",
      shouldTriggerReRender: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleAddressChange = this.handleAddressChange.bind(this);
    this.handlerAddBuildingError = this.handlerAddBuildingError.bind(this);
    this.toggleModal = this.toggleModal.bind(this);
  }

  handleAddressChange(event) {
    this.setState({
      buildingAddressValue: event.target.value,
    });
  }

  handleNameChange(event) {
    this.setState({
      buildingNameValue: event.target.value,
    });
  }

  toggleModal() {
    this.setState((prevState) => {
      if (prevState.modalIsOpen && !prevState.isSendingAsyncRequest) {
        if (prevState.shouldTriggerReRender) {
          this.props.reRenderParentCallbackUpdateBuildingsFloors();
        }
        return {
          modalIsOpen: !prevState.modalIsOpen,
          isSendingAsyncRequest: false,
          error_asyncRequest: false,
          success_asyncRequest: false,
          buildingNameValue: "",
          buildingAddressValue: "",
          shouldTriggerReRender: false,
        };
      } else {
        return {
          ...prevState,
          modalIsOpen: !prevState.modalIsOpen,
        };
      }
    });
  }

  handleSubmit(event) {
    this.setState(
      {
        isSendingAsyncRequest: true,
      },
      () => {
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            address: this.state.buildingAddressValue,
            buildingName: this.state.buildingNameValue,
          }),
        };
        return fetch(`${backendURL}buildings`, requestOptions)
          .then((res) => {
            if (res.ok) {
              this.setState({
                success_asyncRequest: true,
                isSendingAsyncRequest: false,
                error_asyncRequest: false,
                shouldTriggerReRender: true,
              });
            } else {
              this.handlerAddBuildingError();
            }
          })
          .catch((error) => {
            return this.handlerAddBuildingError();
          });
      }
    );

    event.preventDefault();
  }

  statusMessage() {
    if (this.state.error_asyncRequest && this.state.modalIsOpen) {
      return (
        <div className="textAddBuilding">
          {" "}
          Unable to add building. Please try again.{" "}
        </div>
      );
    } else {
      return false;
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (
      this.props.addBuildingButtonSimulator !==
        prevProps.addBuildingButtonSimulator &&
      prevState.modalIsOpen === false
    ) {
      this.setState({ modalIsOpen: true });
    }
  }

  handlerAddBuildingError() {
    this.setState({
      isSendingAsyncRequest: false,
      success_asyncRequest: false,
      error_asyncRequest: true,
    });
  }

  render() {
    return (
      <div>
        <Modal isOpen={this.state.modalIsOpen} toggle={this.toggleModal}>
          <ModalHeader toggle={this.toggleModal}>New Building</ModalHeader>

          {this.state.success_asyncRequest ? (
            <ModalBody>
              <div> The building was successfully added. </div>
            </ModalBody>
          ) : (
            <ModalBody>
              {this.state.modalIsOpen ? (
                <>
                  {" "}
                  <div class="input-group mb-3">
                    <span class="input-group-text" id="basic-addon1">
                      Name
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Give the name of the new building"
                      //added by me
                      value={this.state.buildingNameValue}
                      onChange={this.handleNameChange}
                      disabled={
                        this.state.isSendingAsyncRequest ||
                        this.state.success_asyncRequest
                      }
                      aria-label="Username"
                      aria-describedby="basic-addon1"
                    ></input>
                  </div>
                  <div class="input-group mb-3">
                    <span class="input-group-text" id="basic-addon1">
                      Address
                    </span>
                    <input
                      type="text"
                      class="form-control"
                      placeholder="Give the address of the new building"
                      //added by me
                      value={this.state.buildingAddressValue}
                      disabled={
                        this.state.isSendingAsyncRequest ||
                        this.state.success_asyncRequest
                      }
                      onChange={this.handleAddressChange}
                      aria-label="Username"
                      aria-describedby="basic-addon1"
                    ></input>
                  </div>{" "}
                </>
              ) : (
                false
              )}
            </ModalBody>
          )}

          <ModalFooter>
            {this.statusMessage()}
            {this.state.success_asyncRequest ? (
              false
            ) : (
              <Button
                color="primary"
                onClick={this.handleSubmit.bind(this)}
                disabled={
                  // shouldn't be disabled bc of first condition if user can see it, but just in case
                  !this.state.buildingAddressValue ||
                  !this.state.buildingNameValue ||
                  this.state.isSendingAsyncRequest ||
                  this.state.success_asyncRequest
                }
              >
                {this.state.isSendingAsyncRequest
                  ? "Processing..."
                  : "Add Building"}
              </Button>
            )}
            <Button color="secondary" onClick={this.toggleModal}>
              Exit
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}
