#
# naivebayes.R - a simple Naive Bayes spam filter 
# (Solution to CSE312 17wi hw4, problem 5)
#

# Method: given two directories, one each of (hand-labeled) spam and 
#   non-spam ("ham") emails, we estimate ... (more detail to come...)
#   Each message in a test set directory is classified based on whether 
#   its contents favors S or H based on unique words contained in it,
#   based on ...(more detail to come...).
#
# Output: a list of test message file names and predicted classifications, 
#   one pair per line, plus some ancillary summaries.
#
# Input: three directory names, built-in as constants below, specifying the 
#   directories containing training spam, training non-spam and test messages.

dir.train.spam <- 'data/train/spam/'
dir.train.ham  <- 'data/train/ham/'
dir.test       <- 'data/test/'

####
#
# token_set(filename) - Given a file (full path or cwd-relative), return   
#   a vector containing the unique tokens (i.e., white-space-separated 
#   strings) in the file.
#
# E.g., given a file containing:
#
# Subject: Get rid of your student loans
# Hi there,
# If you work for us, we will give you money
# to repay your student loans. You will be
# debt free!
# FakePerson_22393
#
# This function would return the length 26 vector:
#
# [1] "Subject:"         "Get"              "rid"              "of"               "your"            
# [6] "student"          "loans"            "Hi"               "there,"           "If"              
#[11] "you"              "work"             "for"              "us,"              "we"              
#[16] "will"             "give"             "money"            "to"               "repay"           
#[21] "loans."           "You"              "be"               "debt"             "free!"           
#[26] "FakePerson_22393"
# 
token_set <- function(filename){
	return(unique(scan(file=filename,what='character',quote='',quiet=TRUE)))
}

####
#
# You code goes here...
#
###
