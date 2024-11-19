function Sig(x) {
    return 1 / (1 + Math.exp(-x));
}
function Soft(array) {
    let soft_result = [];
    let soft_array = array.map(Math.exp);
    let soft_sum = soft_array.reduce((ei, eii) => ei + eii, 0);
    for (let j = 0; j < soft_array.length; j++) {
        soft_result[j] = soft_array[j] / soft_sum;
    }

    return soft_result;
}
function Tanh(x) {
    return Math.tanh(x);
}
function ReLU(x) {
    if (x>=0){
        return x;
    }
    else return 0;
}
function hidden_activation_Functions(netInput, activationType) {
    switch (activationType) {
        case 'Tanh':
            return Tanh(netInput);
        case 'ReLU':
            return ReLU(netInput);
        case 'Sigmoid':
            return Sig(netInput);
        default:
            return Sig(netInput);
    }
}
function tanh_derivative(y) {
    return 1 - (y * y);
}
function relu_derivative(y) {
    if(y>0){
        return 1;
    }else{
        return 0;
    }
}
function sigmoid_derivative(y){
    return y*(1-y);
}
function softmax_derivative(softmaxValues, i, j) {
    if (i === j) {
        return softmaxValues[i] * (1 - softmaxValues[i]);
    } else {
        return -softmaxValues[i] * softmaxValues[j];
    }
}
function activations_dev(Y, activationType){
    switch (activationType) {
        case 'Tanh':
            return tanh_derivative(Y);
        case 'ReLU':
            return relu_derivative(Y);
        case 'Sigmoid':
            return sigmoid_derivative(Y);
        default:
            return relu_derivative(Y);
    }
}
function initialization_step(inputSize, hiddenSize, outputSize) {
    const input_hidden_weights = [];
    const hidden_biases = [];
    for (let i = 0; i < hiddenSize; i++) {
        input_hidden_weights[i] = Math.random() * (4.8 / inputSize) - 2.4 / inputSize;
        hidden_biases[i] = Math.random() * (4.8 / inputSize) - 2.4 / inputSize;
    }
    const hidden_output_weights = [];
    const output_biases = [];
    for (let i = 0; i < outputSize; i++) {
        hidden_output_weights[i] = Math.random() * (4.8 / hiddenSize) - 2.4 / hiddenSize;
        output_biases[i] = Math.random() * (4.8 / hiddenSize) - 2.4 / hiddenSize;
    }

    return { input_hidden_weights,hidden_biases,  hidden_output_weights, output_biases };
}
function activation_step(inputs, parameters, hidden_activation_type) {
    const { input_hidden_weights, hidden_biases, hidden_output_weights, output_biases } = parameters;
    const hidden_result_activation = [];
    for (let i = 0; i < input_hidden_weights.length; i++) {
        let X1 = 0;
        for (let j = 0; j < inputs.length; j++) {
            X1 += input_hidden_weights[i] * inputs[j];
        }
        X1 -= hidden_biases[i];
        hidden_result_activation[i] = hidden_activation_Functions(X1, hidden_activation_type);
    }

    const output_result_activation = [];
    for (let i = 0; i < hidden_output_weights.length; i++) {
        let X2 = 0;
        for (let j = 0; j < hidden_result_activation.length; j++) {
            X2 += hidden_output_weights[i] * hidden_result_activation[j];
        }
        output_result_activation[i] = X2 + output_biases[i];
    }
    const outputActivations = Soft(output_result_activation);
    return { hidden_result_activation, outputActivations };
}
function training_step(inputs, label, parameters, learning_rate, fruit_labels, goal, activation_type) {
    const activation_results = activation_step(inputs, parameters);
    const hidden_result_activation = activation_results.hidden_result_activation;
    const output_activations = activation_results.outputActivations;
    const labels = [];
    for (let i = 0; i < fruit_labels.length; i++) {
        labels[i] = fruit_labels[i] === label ? 1 : 0;
    }
    const output_errors = [];
    for (let k = 0; k < output_activations.length; k++) {
        output_errors[k] = labels[k] - output_activations[k];
    }
    const outputGradients = [];
    for (let i = 0; i < output_activations.length; i++) {
        const outputError = output_errors[i];
        const softmaxDerivatives = [];
        for (let j = 0; j < output_activations.length; j++) {
            softmaxDerivatives[j] = softmax_derivative(output_activations, i, j);
        }
        outputGradients[i] = softmaxDerivatives[i] * outputError;
    }
    const hiddenGradients = [];
    for (let i = 0; i < hidden_result_activation.length; i++) {
        const ya = hidden_result_activation[i];
            let weightedSum = 0;

            for (let j = 0; j < outputGradients.length; j++) {
                weightedSum += outputGradients[j] * parameters.hidden_output_weights[j];
            }
        hiddenGradients[i] = activations_dev(ya,activation_type)* weightedSum;
    }
    for (let i = 0; i < parameters.hidden_output_weights.length; i++) {
        parameters.hidden_output_weights[i] += learning_rate * outputGradients[i] * hidden_result_activation[i];
        parameters.output_biases[i] += learning_rate * outputGradients[i];
    }

    for (let i = 0; i < inputs.length; i++) {
        parameters.input_hidden_weights[i] += learning_rate * hiddenGradients[i] * inputs[i];
        parameters.hidden_biases[i] += learning_rate * hiddenGradients[i];
    }
    let G = 0;
    if (goal === 'MSE') {
        for (let i = 0; i < output_errors.length; i++) {
            G += Math.pow(output_errors[i], 2);
        }
        G /= output_errors.length;
    } else if (goal === 'CrossEntropy') {
            for (let j = 0; j < labels.length; j++) {
                G += -labels[j] * Math.log(output_activations[j]);
            }
    }
    return {G,output_errors};
}
function samples_of_data(iterations, data, parameters, learningRate, fruit_labels, goal, performance, activation_type) {
    const table = document.querySelector('table');

    for (let iteration = 0; iteration < iterations; iteration++) {
        for (let i = 0; i < data.length; i++) {
            const sample = data[i];
            const inputs = sample.slice(0, sample.length - 1);
            const label = sample[sample.length - 1];
            const r = training_step(inputs, label, parameters, learningRate, fruit_labels, goal, activation_type);
            const activation_results = activation_step(inputs, parameters, activation_type);
            const predictedClassIndex = predict(activation_results.outputActivations);
            const predictedClassLabel = fruit_labels[predictedClassIndex];
            const output_errors = r.output_errors;
            const m = r.G;
            const row = table.rows[i + 1];
            row.cells[3].textContent = predictedClassLabel;
            for (let j = 0; j < output_errors.length; j++) {
                row.cells[4].textContent = output_errors[j].toFixed(2);
            }
            row.cells[5].textContent = m.toFixed(2);
            if (goal === 'MSE') {
                performance.mse.push(m);
            } else if (goal === 'CrossEntropy') {
                performance.crossEntropy.push(m);
            }
        }
    }
}
function calculate_accuracy(data, parameters, activationType, classLabels) {
    let correctPredictions = 0;
    for (let i = 0; i < data.length; i++) {
        const sample = data[i];
        const inputs = sample.slice(0, -1);
        const label = sample[sample.length - 1];

        const { outputActivations } = activation_step(inputs, parameters, activationType);
        const predictedClassIndex = predict(outputActivations);
        const predictedClassLabel = classLabels[predictedClassIndex];

        if (predictedClassLabel === label) {
            correctPredictions++;
        }
    }

    const accuracy = correctPredictions / data.length;
    return accuracy;
}
function predict(outputActivations) {
    let maxActivation = outputActivations[0];
    let maxIndex = 0;
    for (let i = 1; i < outputActivations.length; i++) {
        if (outputActivations[i] > maxActivation) {
            maxActivation = outputActivations[i];
            maxIndex = i;
        }
    }

    return maxIndex;
}
function train() {
    const activationType = localStorage.getItem('activationType');
    const goal = localStorage.getItem('goal');
    const epochs = parseInt(localStorage.getItem('epochs'), 10);
    const learningRate = parseFloat(localStorage.getItem('learningRate'));
    const neurons = parseInt(localStorage.getItem('neurons'), 10);
    const data = [
        [5, 1, 'Apple'],
        [7, 2, 'Banana'],
        [1, 3, 'Orange'],
        [6, 1, 'Apple'],
        [8, 2, 'Banana'],
        [2, 3, 'Orange'],
        [7, 1, 'Apple'],
        [3, 3, 'Orange'],
        [9, 2, 'Banana'],
        [8, 1, 'Apple'],
        [10, 2,'Banana'],
        [4, 3, 'Orange'],
    ];
    const fruit_labelsSet = new Set();
    for (const entry of data) {
        fruit_labelsSet.add(entry[2]);
    }
    const performance = {
        mse: [],
        crossEntropy: [],
    };
    const fruit_labels = Array.from(fruit_labelsSet);
    const outputSize = fruit_labels.length;
    const parameters = initialization_step(2, neurons, outputSize);
    const iterations = epochs;

    samples_of_data(iterations, data, parameters, learningRate, fruit_labels, goal, performance, activationType);
    return {data,parameters,fruit_labels,performance}

}
function test(){
const t=train();
const data=t.data;
const parameters=t.parameters;
const fruit_labels=t.fruit_labels;
    const performance =t.performance;

    const activation_type = localStorage.getItem('activationType');
    const goal = localStorage.getItem('goal');
    const accuracy = calculate_accuracy(data, parameters, activation_type, fruit_labels);
    const accuracy_element = document.getElementById('accuracy');
    accuracy_element.textContent =  (accuracy * 100).toFixed(2) + '%';
    const performanceElement = document.getElementById('performance');
    if (goal === 'MSE') {
        const mse_value = performance.mse.length > 0 ? performance.mse[performance.mse.length - 1] : 'N/A';
        performanceElement.textContent =  (mse_value*100).toFixed(2)+"%";
    } else if (goal === 'CrossEntropy') {
        const crossEntropy_value = performance.crossEntropy.length > 0 ? performance.crossEntropy[performance.crossEntropy.length - 1] : 'N/A';
        performanceElement.textContent =  crossEntropy_value*100+"%";
    }
    const color_label=document.getElementById('color').value;
    let c=1;
    if(color_label==='Red'){
        c=1;
    }
    else if (color_label==='Orange'){
        c=2;
    }
    else if (color_label==='Yellow'){
        c=3;
    }
    const s=document.getElementById('sweet').value;
    const data_for_test = { sweetness: s, color: c };
    const { outputActivations } = activation_step([data_for_test.sweetness, data_for_test.color], parameters, activation_type);
    const predicted_index = predict(outputActivations);
    const predicted_label = fruit_labels[predicted_index];
    const result_element = document.getElementById('fruit');
    result_element.textContent =  ''+predicted_label ;
}