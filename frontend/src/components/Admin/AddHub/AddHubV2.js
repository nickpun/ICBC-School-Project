import React, { Component } from "react";
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import {
  FLOORPLAN_PIXEL_WIDTH,
  FLOORPLAN_PIXEL_HEIGHT,
} from "../../../utils/image_metadata";
import { backendURL } from "../../../utils/urlLinks";

export default class AddHubV2 extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modalIsOpen: false,
      isSendingAsyncRequest: false,
      success_asyncRequest: false,
      error_asyncRequest: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handlerAddHubError = this.handlerAddHubError.bind(this);
    this.toggleModal = this.toggleModal.bind(this);
  }

  toggleModal() {
    this.setState((prevState) => {
      if (prevState.modalIsOpen && !prevState.isSendingAsyncRequest) {
        this.props.resetAddHubMode();
        return {
          modalIsOpen: !prevState.modalIsOpen,
          isSendingAsyncRequest: false,
          error_asyncRequest: false,
          success_asyncRequest: false,
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
            floorId: this.props.floorId,
            xlocs: [
              Math.floor(
                FLOORPLAN_PIXEL_WIDTH *
                  this.props.clickedImageProportions.xProportion
              ),
            ],
            ylocs: [
              Math.floor(
                FLOORPLAN_PIXEL_HEIGHT *
                  this.props.clickedImageProportions.yProportion
              ),
            ],
          }),
        };
        return fetch(`${backendURL}hubs`, requestOptions)
          .then((res) => {
            if (res.ok) {
              this.setState({
                success_asyncRequest: true,
                isSendingAsyncRequest: false,
                error_asyncRequest: false,
              });
              this.props.reRenderParentCallbackUpdateFloor();
            } else {
              return this.handlerAddHubError();
            }
          })
          .catch(() => this.handlerAddHubError());
      }
    );

    event.preventDefault();
  }

  statusMessage() {
    if (this.state.error_asyncRequest && this.state.modalIsOpen) {
      return <label> The hub could not be added. </label>;
    } else {
      return false;
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (
      this.props.imageClickSimulator !== prevProps.imageClickSimulator &&
      prevState.modalIsOpen === false &&
      this.props.addHubMode === true
    ) {
      this.setState({ modalIsOpen: true });
    }
  }

  handlerAddHubError() {
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
          <ModalHeader toggle={this.toggleModal}>New Hub</ModalHeader>

          {this.state.success_asyncRequest ? (
            <ModalBody>
              <div> The hub was successfully added </div>
            </ModalBody>
          ) : (
            <ModalBody>
              {this.state.modalIsOpen ? (
                <label>Add a hub to this location? </label>
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
                  !this.props.clickedImageProportions ||
                  this.state.isSendingAddHubRequest ||
                  this.state.success_asyncRequest
                }
              >
                {this.state.isSendingAddHubRequest
                  ? "Processing..."
                  : "Confirm"}
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
