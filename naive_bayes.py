# This function reads in a file and returns a 
# set of all the tokens. It ignores the subject line

# If the email had the following content:

# Subject: Get rid of your student loans
# Hi there,
# If you work for us, we will give you money
# to repay your student loans. You will be
# debt free!
# FakePerson_22393

# This function would return to you
# set(['', 'work', 'give', 'money', 'rid', 'your', 'there,', 'for', 'Get', 'to', 'Hi', 'you', 'be', 'we', 'student', 'debt', 'loans', 'loans.', 'of', 'us,', 'will', 'repay', 'FakePerson_22393', 'free!', 'You', 'If'])

def token_set(filename):
	#open the file handle
	with open(filename, 'r') as f:
		#ignore the Subject beginning
		text = f.read()[9:]
		#put it all on one line
		text = text.replace('\r', '')
		text = text.replace('\n', ' ')
		#split by spaces
		tokens = text.split(' ')
		#return the set of unique tokens
		return set(tokens)

# TODO: Implement the Naive Bayes Classifier
